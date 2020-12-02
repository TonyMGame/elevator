package com.soundai.elevator.cms.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.common.ResultEnum;
import com.soundai.elevator.cms.entity.Equipment;
import com.soundai.elevator.cms.entity.EquipmentListByLocation;
import com.soundai.elevator.cms.entity.PushTask;
import com.soundai.elevator.cms.entity.User;
import com.soundai.elevator.cms.mapper.EquipmentMapper;
import com.soundai.elevator.cms.mapper.PushTaskMapper;
import com.soundai.elevator.cms.service.DirectivePush;
import com.soundai.elevator.cms.service.EquipmentService;
import com.soundai.elevator.cms.task.CachePool;
import com.soundai.elevator.cms.task.LocalCache;
import com.soundai.elevator.cms.task.ThreadRepertory;
import com.soundai.elevator.cms.util.OkHttp3;
import com.soundai.elevator.cms.util.Signature;
import com.soundai.elevator.cms.vo.EquipmentPageVo;
import com.soundai.elevator.cms.vo.EquipmentVo;
import com.soundai.elevator.cms.vo.PageList;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 常磊
 * @date 4.20
 * @describe 设备管理
 */
@Log4j2
@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Resource
    private EquipmentMapper equipmentMapper;
    @Resource
    private OkHttp3 okHttp3;
    @Value("${azero.bind-url}")
    private String bindUrl;
    @Value("${azero.appId}")
    private String appId;
    @Value("${azero.secretKey}")
    private String secretKey;
    @Value("${azero.productId}")
    private String productId;
    @Resource
    private DirectivePush directivePush;
    @Resource
    private PushTaskMapper pushTaskMapper;


    /**
     * 获取设备列表
     *
     * @param equipmentPageVo
     * @return
     * @throws BusinessException
     */
    @Override
    public PageList getEquipmentList(EquipmentPageVo equipmentPageVo) throws BusinessException {
        User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
        if (user.getLevel() != 2) {
            equipmentPageVo.setUserId(user.getId());
        }
        Page page = PageHelper.startPage(equipmentPageVo.getPageNum(), equipmentPageVo.getPageSize());
        List<Equipment> materialList = this.equipmentMapper.getEquipmentList(equipmentPageVo);
        return new PageList(equipmentPageVo.getPageNum(),
                equipmentPageVo.getPageSize(),
                page.getTotal(), page.getPages(),
                materialList);
    }

    /**
     * 添加设备
     *
     * @param equipmentVo
     * @throws BusinessException
     */
    @Override
    public void insertEquipment(EquipmentVo equipmentVo) throws BusinessException {
        int num = this.equipmentMapper.getEquipmentBySn(equipmentVo.getDeviceSN());
        if (num > 0) {
            throw new BusinessException(ResultEnum.ADD_DEVICE_SN_ERROR);
        }
        String res = deviceBind(productId, equipmentVo.getDeviceSN());
        JSONObject resJson = JSON.parseObject(res);
        log.info("deviceBind res： {}", resJson);
        int code = Integer.parseInt(resJson.getString("code"));
        if (code != 200) {
            throw new BusinessException(ResultEnum.DIND_DEVICE_ERROR);
        }
        String deviceId = JSON.parseObject(resJson.getString("data")).getString("deviceId");
        equipmentVo.setEquipmentId(deviceId);
        try {
            User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
            equipmentVo.setUserId(user.getId());
            equipmentMapper.insertEquipment(equipmentVo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.INSERT_DATA_ERROR);
        }
    }


    /**
     * 绑定设备
     *
     * @param deviceSN
     * @param productId
     * @return
     * @throws BusinessException
     */
    public String deviceBind(String deviceSN, String productId) throws BusinessException {
        try {
            User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
            String json = handleStr(productId, deviceSN, user.getUserId());
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                    , json);
            Request request = new Request.Builder()
                    .header("Authorization", "SaiApp " + appId + ":" + Signature.signatureStr(appId, secretKey))
                    .url(bindUrl)
                    .post(requestBody)
                    .build();
            Response response = okHttp3.getOkHttpClient().newCall(request).execute();
            String res = response.body().string();
            log.info("deviceBind res{}", res);
            return res;
        } catch (Exception e) {
            throw new BusinessException(ResultEnum.REQUEST_ERROR);
        }

    }

    public String handleStr(String deviceSN, String productId, String userId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceSN", deviceSN);
        jsonObject.put("productId", productId);
        jsonObject.put("userId", userId);
        String jsonStr = jsonObject.toJSONString();
        log.info("str {}", jsonStr);
        return jsonStr;
    }

    /**
     * 删除设备
     *
     * @param id
     * @throws BusinessException
     */
    @Override
    @Transactional
    public void delEquipment(int id) throws BusinessException {
        Equipment equipment= equipmentMapper.getEquipment(id);
        List<Map<String,Object>> sources = new ArrayList<>();
        LocalCache.remove(equipment.getDeviceId());
        this.pushTaskMapper.deleteDefaultPushTaskByDeviceId(equipment.getDeviceId());
        this.pushTaskMapper.deletePushTaskByDeviceId(equipment.getDeviceId());
        this.equipmentMapper.delEquipment(id);
        this.directivePush.pushMassage(equipment.getDeviceId(), sources);
    }

    /**
     * 查询设备包含的全部地点
     *
     * @return
     * @throws BusinessException
     */
    public List<String> getEquipmentLocationList() throws BusinessException {
        User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
        if(user.getLevel()==2){
            return this.equipmentMapper.getEquipmentLocationList(null);
        }
        return this.equipmentMapper.getEquipmentLocationList(user.getId().toString());
    }

    /**
     * 根据地点获取全部设备
     *
     * @param location
     * @return
     */
    @Override
    public List<Equipment> getElevatorList(ArrayList<String> location) {
        try {
            User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
            EquipmentListByLocation equipmentListByLocation =  new EquipmentListByLocation();
            equipmentListByLocation.setUserId(user.getId());
            equipmentListByLocation.setLocation(location);
            log.info(JSON.toJSONString(equipmentListByLocation));
            return this.equipmentMapper.getEquipmentListByLocation(equipmentListByLocation);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Equipment> getDefElevatorList(ArrayList<String> location) {
        User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
        EquipmentListByLocation equipmentListByLocation =  new EquipmentListByLocation();
        equipmentListByLocation.setUserId(user.getId());
        equipmentListByLocation.setLocation(location);
        return this.equipmentMapper.getDefEquipmentListByLocation(equipmentListByLocation);
    }

}
