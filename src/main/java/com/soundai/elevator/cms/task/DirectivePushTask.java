package com.soundai.elevator.cms.task;

import com.alibaba.fastjson.JSON;
import com.soundai.elevator.cms.entity.ChangeDefaultStatus;
import com.soundai.elevator.cms.entity.Material;
import com.soundai.elevator.cms.mapper.MaterialMapper;
import com.soundai.elevator.cms.mapper.PushTaskMapper;
import com.soundai.elevator.cms.service.DirectivePush;
import com.soundai.elevator.cms.service.PushTaskService;
import com.soundai.elevator.cms.vo.ChangeValueVo;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 常磊
 * @date 4.22
 * @describe 定时任务
 */
@Log4j2
@Component
@EnableScheduling
public class DirectivePushTask {

    @Resource
    DirectivePush directivePush;
    @Resource
    PushTaskService pushTaskService;
    @Resource
    private PushTaskMapper pushTaskMapper;
    @Resource
    private MaterialMapper materialMapper;

    @Scheduled(fixedRate = 1000 * 30 * 1)
    public void runStart() {
        log.info("开始下发任务");
        List<Map> tasks = pushTaskMapper.waitePushTask();
        if (tasks.size() == 0) {
            return;
        }
        try {
            //全部素材
            Map param = new HashMap();
            param.put("type", 0);
            List<Material> materialList = materialMapper.materialDictionary(param);
            Map<String, Object> materialMap = new HashMap();
            for (Material materialBean : materialList) {
                materialMap.put(String.valueOf(materialBean.getId()), materialBean);
            }
            for (Map pushTask : tasks) {
                //需要的素材集合
                List<Map<String, Object>> sources = new ArrayList<>();
                //素材
                String material = String.valueOf(pushTask.get("material"));
                String[] materialId = material.split(",");
                for (String id : materialId) {
                    Material material1 = (Material) materialMap.get(id);
                    Map sourcemap = new HashMap();
                    sourcemap.put("url", material1.getUrl());
                    sourcemap.put("type", material1.getType());
                    sourcemap.put("duration", pushTask.get("duration"));
                    sources.add(sourcemap);
                }
                log.info("materialList sources {}", sources);
                this.directivePush.pushMassage(String.valueOf(pushTask.get("device_id")), sources);
                //更改任务的状态
                ChangeValueVo changeValueVo = new ChangeValueVo();
                changeValueVo.setId(Integer.valueOf(pushTask.get("id").toString()));
                changeValueVo.setItem(1); //进行中
                pushTaskService.changeStatus(changeValueVo);
                //更新默认为使用
                ChangeDefaultStatus changeDefaultStatus =new  ChangeDefaultStatus();
                changeDefaultStatus.setDeviceId(String.valueOf(pushTask.get("device_id")));
                changeDefaultStatus.setValue("0");
                this.pushTaskService.changeDefaultStatus(changeDefaultStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Scheduled(cron = "0/60 * * * * ? ")
    public void runDefault() {
        log.info("开始下发默认任务");
        try {
            //查询进行中的任务
            List<Map> tasks = pushTaskMapper.completePushTask();
            for (Map pushTask : tasks) {
                //发送默认素材或者素材
                String deviceId = pushTask.get("device_id").toString();
                List<Map<String, Object>> sources = new ArrayList<>();
                if(LocalCache.get(deviceId)!=null){
                    sources = (List<Map<String, Object>>) LocalCache.get(deviceId);
                }
                this.directivePush.pushMassage(deviceId, sources);
                //更新为任务完成
                ChangeValueVo changeValueVo = new ChangeValueVo();
                changeValueVo.setId(Integer.valueOf(pushTask.get("id").toString()));
                changeValueVo.setItem(2); //结束
                this.pushTaskService.changeStatus(changeValueVo);
                //更新默认为使用
                ChangeDefaultStatus changeDefaultStatus =new  ChangeDefaultStatus();
                changeDefaultStatus.setDeviceId(deviceId);
                changeDefaultStatus.setValue("1");
                this.pushTaskService.changeDefaultStatus(changeDefaultStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
