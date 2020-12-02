package com.soundai.elevator.cms.task;

import com.soundai.elevator.cms.entity.Material;
import com.soundai.elevator.cms.mapper.MaterialMapper;
import com.soundai.elevator.cms.mapper.PushTaskMapper;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 常磊
 * @date 4.23
 * @describe 缓存默认任务
 */
@Log4j2
@Component
public class InitDefaultTask implements CommandLineRunner {

    @Resource
    private PushTaskMapper PushTaskMapper;
    @Resource
    private MaterialMapper materialMapper;

    /**
     * 初始化默认任务 将任务资源放入缓存
     *
     * @param args
     */
    @Override
    public void run(String... args) {
        try {
            log.info("初始化默认任务");
            //是否有默认任务
            List<Map> mapList = this.PushTaskMapper.getDefaultTask();
            if (mapList.size() == 0) {
                return;
            }
            //全部素材
            Map param = new HashMap();
            param.put("type", 0);
            List<Material> materialList = materialMapper.materialDictionary(param);
            //全部素材转成map数据
            Map materialMap = new HashMap();
            for (Material materialBean : materialList) {
                materialMap.put(String.valueOf(materialBean.getId()), materialBean);
            }
            //遍历默认任务
            for (Map map : mapList) {
                //需要放到缓存池的素材集合
                List<Map> resList = new ArrayList<>();
                //素材id数组
                String[] materialId = String.valueOf(map.get("material")).split(",");
                //遍历id 查找素材
                for (String id : materialId) {
                    Map sourcemap = new HashMap();
                    Material material = (Material) materialMap.get(id);
                    if(material==null){
                        log.info("初始化默认任务 err 素材为空 {}:",id);
                        break;
                    }
                    sourcemap.put("url", material.getUrl());
                    sourcemap.put("type", material.getType());
                    sourcemap.put("duration", map.get("duration"));
                    resList.add(sourcemap);
                }
                LocalCache.set(map.get("device_id"), resList);
            }
        } catch (Exception e) {
            log.error("InitDefaultTask缓存默认任务 err");
            e.printStackTrace();
        }
    }
}
