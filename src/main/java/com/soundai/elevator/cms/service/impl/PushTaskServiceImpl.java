package com.soundai.elevator.cms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.common.ResultEnum;
import com.soundai.elevator.cms.entity.*;
import com.soundai.elevator.cms.mapper.EquipmentMapper;
import com.soundai.elevator.cms.mapper.MaterialMapper;
import com.soundai.elevator.cms.mapper.PushTaskMapper;
import com.soundai.elevator.cms.service.PushTaskService;
import com.soundai.elevator.cms.task.CachePool;
import com.soundai.elevator.cms.task.DirectivePushTask;
import com.soundai.elevator.cms.task.LocalCache;
import com.soundai.elevator.cms.task.ThreadRepertory;
import com.soundai.elevator.cms.vo.*;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author 常磊
 * @date 4.20
 * @describe 推送任务管理
 */
@Log4j2
@Service
public class PushTaskServiceImpl implements PushTaskService {

    @Resource
    private PushTaskMapper pushTaskMapper;

    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private EquipmentMapper equipmentMapper;
    @Resource
    private DirectivePushTask directivePushTask;

    @Override
    public PageList getPushTaskList(PushTaskPageVo pushTaskPageVo) throws BusinessException {
        try {
            pushTaskPageVo.setStatus(1);
            Page page = PageHelper.startPage(pushTaskPageVo.getPageNum(), pushTaskPageVo.getPageSize());
            User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
            if (user.getLevel() != 2) {
                pushTaskPageVo.setUserId(user.getId());
            }
            List<PushTask> pushTaskList = this.pushTaskMapper.getPushTaskList(pushTaskPageVo);

            Map param = new HashMap();
            param.put("type", 0);
            if (user.getLevel() != 2) {
                param.put("userId", user.getId());
            }
            List<Material> materialList = materialMapper.materialDictionary(param);
            Map<String, Object> materialMap = new HashMap();
            for (Material materialBean : materialList) {
                materialMap.put(String.valueOf(materialBean.getId()), materialBean);
            }

            for (PushTask PushTask : pushTaskList) {
                StringBuilder str = new StringBuilder();
                String[] material = PushTask.getMaterial().split(",");
                for (String id : material) {
                    Material materialBean = (Material) materialMap.get(id);
                    if (materialBean != null) {
                        if ("".equals(str.toString())) {
                            str.append(materialBean.getName());
                        } else {
                            str.append(",");
                            str.append(materialBean.getName());
                        }
                    }
                }
                PushTask.setMaterialName(str.toString());
            }

            return new PageList(pushTaskPageVo.getPageNum(),
                    pushTaskPageVo.getPageSize(),
                    page.getTotal(), page.getPages(),
                    pushTaskList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.QUERY_DATA_ERROR);
        }

    }

    @Override
    public PageList getHistoryPushTaskList(PushTaskPageVo pushTaskPageVo) throws BusinessException {
        try {
            pushTaskPageVo.setStatus(2);
            User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
            Page page = PageHelper.startPage(pushTaskPageVo.getPageNum(), pushTaskPageVo.getPageSize());
            if (user.getLevel() != 2) {
                pushTaskPageVo.setUserId(user.getId());
            }
            List<PushTask> pushTaskList = this.pushTaskMapper.getPushTaskListH(pushTaskPageVo);

            Map param = new HashMap();
            param.put("type", 0);
            if (user.getLevel() != 2) {
                param.put("userId", user.getId());
            }
            List<Material> materialList = materialMapper.materialDictionary(param);
            Map<String, Object> materialMap = new HashMap();
            for (Material materialBean : materialList) {
                materialMap.put(String.valueOf(materialBean.getId()), materialBean);
            }

            for (PushTask PushTask : pushTaskList) {
                StringBuilder str = new StringBuilder();
                String[] material = PushTask.getMaterial().split(",");
                for (String id : material) {
                    Material materialBean = (Material) materialMap.get(id);
                    if (materialBean != null) {
                        if ("".equals(str.toString())) {
                            str.append(materialBean.getName());
                        } else {
                            str.append(",");
                            str.append(materialBean.getName());
                        }
                    }
                }
                PushTask.setMaterialName(str.toString());
            }

            return new PageList(pushTaskPageVo.getPageNum(),
                    pushTaskPageVo.getPageSize(),
                    page.getTotal(), page.getPages(),
                    pushTaskList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.QUERY_DATA_ERROR);
        }
    }


    /**
     * 添加任务
     *
     * @param pushTaskVo
     * @throws BusinessException
     */
    @Override
    public void add(PushTaskVo pushTaskVo) throws BusinessException {
        String[] ids = pushTaskVo.getElevatorId().split(",");
        List<Equipment> equipmentList = this.equipmentMapper.getEquipmentListByIds(ids);
        log.info("materialList {}", equipmentList);
        Map<Integer, Equipment> map = listToMapOne(equipmentList);
        List<PushTask> list = new ArrayList<>();
        User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
        Long taskId = new Date().getTime() + user.getId();
        RepeatTime repeatTime = new RepeatTime();
        List<String> deviceIds = new ArrayList<>();
        for (String id : ids) {
            Equipment equipment = map.get(Integer.parseInt(id));
            PushTask PushTask = new PushTask();
            PushTask.setTaskId(taskId);
            PushTask.setName(pushTaskVo.getName());
            PushTask.setMaterial(pushTaskVo.getMaterial());
            PushTask.setDeviceId(equipment.getDeviceId());
            PushTask.setEdTime(new Date(pushTaskVo.getEdTime()));
            PushTask.setBgTime(new Date(pushTaskVo.getBgTime()));
            PushTask.setStatus(0);
            PushTask.setUpdateUserName(String.valueOf(user.getId()));
            PushTask.setDuration(pushTaskVo.getDuration());
            list.add(PushTask);
            deviceIds.add(equipment.getDeviceId());
            repeatTime.setBgTime(pushTaskVo.getBgTime());
            repeatTime.setEdTime(pushTaskVo.getEdTime());
        }
        repeatTime.setList(deviceIds);
        this.repeatTime(repeatTime);
        try {
            this.pushTaskMapper.add(list);
            this.updateMaterialUsed(pushTaskVo.getMaterial());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.INSERT_DATA_ERROR);
        }
    }

    /**
     * 更新使用的素材
     *
     * @param ids
     */
    void updateMaterialUsed(String ids) {
        List idsArr = new ArrayList();
        for (String id : ids.split(",")) {
            idsArr.add(id);
        }
        this.materialMapper.updateMaterialUsed(idsArr);
    }


    @Override
    public void edit(PushTaskVo pushTaskVo) throws BusinessException {
        RepeatTime repeatTime = new RepeatTime();
        List<String> deviceIds = new ArrayList<>();
        List<PushTask> list = new ArrayList<>();
        try {
            String[] idArr = pushTaskVo.getElevatorId().split(",");
            List<Equipment> equipmentList = this.equipmentMapper.getEquipmentListByIds(idArr);
            Map<Integer, Equipment> map = listToMapOne(equipmentList);
            this.pushTaskMapper.delBatch(pushTaskVo.getTaskId());
            User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
            for (String id : idArr) {
                Equipment equipment = map.get(Integer.parseInt(id));
                PushTask PushTask = new PushTask();
                PushTask.setTaskId(pushTaskVo.getTaskId());
                PushTask.setMaterial(pushTaskVo.getMaterial());
                PushTask.setEdTime(new Date(pushTaskVo.getEdTime()));
                PushTask.setBgTime(new Date(pushTaskVo.getBgTime()));
                PushTask.setStatus(0);
                PushTask.setUpdateUserName(String.valueOf(user.getId()));
                PushTask.setDeviceId(equipment.getDeviceId());
                PushTask.setName(pushTaskVo.getName());
                list.add(PushTask);
                //判断时间
                repeatTime.setBgTime(pushTaskVo.getBgTime());
                repeatTime.setEdTime(pushTaskVo.getEdTime());
                deviceIds.add(equipment.getDeviceId());
            }
            repeatTime.setList(deviceIds);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.INSERT_DATA_ERROR);
        }
        this.repeatTime(repeatTime);
        try {
            this.pushTaskMapper.add(list);
            this.updateMaterialUsed(pushTaskVo.getMaterial());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.INSERT_DATA_ERROR);
        }
    }

    /**
     * 查询是否时间冲突
     *
     * @param repeatTime
     * @throws BusinessException
     */
    void repeatTime(RepeatTime repeatTime) throws BusinessException {
        int num = this.pushTaskMapper.repeatTime(repeatTime);
        if (num != 0) {
            throw new BusinessException(ResultEnum.ADD_TIME_ERROR);
        }
    }

    /**
     * 更改任务状态
     *
     * @param changeValueVo
     * @throws BusinessException
     */
    @Override
    public void changeStatus(ChangeValueVo changeValueVo) throws BusinessException {
        this.pushTaskMapper.changeStatus(changeValueVo);
    }

    @Override
    public void changeDefaultStatus(ChangeDefaultStatus changeDefaultStatus) throws BusinessException {
        this.pushTaskMapper.changeDefaultStatus(changeDefaultStatus);
    }

    @Override
    public void deleteStatus(DeletePushTask deletePushTask) throws BusinessException {
        PushTask pushTask = pushTaskMapper.getPushTask(deletePushTask.getTaskId());
        if (pushTask.getStatus() == 0) {
            this.pushTaskMapper.deletePushTask(deletePushTask);
        } else if (pushTask.getStatus() == 1) {
            this.pushTaskMapper.updateTaskTime(deletePushTask);
            directivePushTask.runDefault();
        }
    }

    /**
     * 默认任务列表
     *
     * @param pushTaskPageVo
     * @throws BusinessException
     */
    @Override
    public PageList getPushTaskDefaultList(PushTaskPageVo pushTaskPageVo) throws BusinessException {
        try {
            User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
            Page page = PageHelper.startPage(pushTaskPageVo.getPageNum(), pushTaskPageVo.getPageSize());
            if (user.getLevel() != 2) {
                pushTaskPageVo.setUserId(user.getId());
            }
            List<PushDefaultTask> pushDefaultTaskList = this.pushTaskMapper.getPushTaskDefaultList(pushTaskPageVo);

            Map param = new HashMap();
            param.put("type", 0);
            if (user.getLevel() != 2) {
                param.put("userId", user.getId());
            }
            List<Material> materialList = materialMapper.materialDictionary(param);
            Map<String, Object> materialMap = new HashMap();
            for (Material materialBean : materialList) {
                materialMap.put(String.valueOf(materialBean.getId()), materialBean);
            }

            for (PushDefaultTask pushDefaultTask : pushDefaultTaskList) {
                String[] ids = pushDefaultTask.getMaterial().split(",");
                StringBuilder str = new StringBuilder();
                for (String id : ids) {
                    Material material = (Material) materialMap.get(id);
                    if (material != null) {
                        if ("".equals(str.toString())) {
                            str.append(material.getName());
                        } else {
                            str.append(",");
                            str.append(material.getName());
                        }
                    }
                }
                pushDefaultTask.setMaterialName(String.valueOf(str));
            }
            return new PageList(pushTaskPageVo.getPageNum(),
                    pushTaskPageVo.getPageSize(),
                    page.getTotal(), page.getPages(),
                    pushDefaultTaskList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.QUERY_DATA_ERROR);
        }
    }

    /**
     * 添加默认方法
     *
     * @param pushDefaultTaskVo
     * @throws BusinessException
     */
    @Override
    public void addDefault(PushDefaultTaskVo pushDefaultTaskVo) throws BusinessException {
        try {
            String material = pushDefaultTaskVo.getMaterial();
            String[] ids = pushDefaultTaskVo.getElevatorId().split(",");
            List<Equipment> equipmentList = this.equipmentMapper.getEquipmentListByIds(ids);
            log.info("materialList {}", equipmentList);
            Map<Integer, Equipment> map = listToMapOne(equipmentList);
            User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
            List<PushDefaultTask> list = new ArrayList<>();
            for (String id : ids) {
                Equipment equipment = map.get(Integer.parseInt(id));
                PushDefaultTask pushDefaultTask = new PushDefaultTask();
                pushDefaultTask.setDeviceId(equipment.getDeviceId());
                pushDefaultTask.setMaterial(material);
                pushDefaultTask.setLocation(equipment.getLocation());
                pushDefaultTask.setElevator(equipment.getId());
                pushDefaultTask.setStatus(0);
                pushDefaultTask.setDuration(pushDefaultTaskVo.getDuration());
                pushDefaultTask.setUpdateUserName(String.valueOf(user.getId()));
                list.add(pushDefaultTask);
            }
            log.info("PushDefaultTask {}", list);
            this.pushTaskMapper.addTaskDefault(list);
            //全部素材
            Map param = new HashMap();
            param.put("type", 0);
            List<Material> materialList = materialMapper.materialDictionary(param);
            //全部素材转成map数据
            Map materialMap = new HashMap();
            for (Material materialBean : materialList) {
                materialMap.put(String.valueOf(materialBean.getId()), materialBean);
            }
            List idsArr = new ArrayList();
            //遍历默认任务
            for (PushDefaultTask pushDefaultTask : list) {
                //需要放到缓存池的素材集合
                List<Map> resList = new ArrayList<>();
                //素材id数组
                String[] materialId = pushDefaultTask.getMaterial().split(",");
                //遍历id 查找素材
                for (String id : materialId) {
                    idsArr.add(id);
                    Map sourcemap = new HashMap();
                    Material materi = (Material) materialMap.get(id);
                    sourcemap.put("url", materi.getUrl());
                    sourcemap.put("type", materi.getType());
                    sourcemap.put("duration", pushDefaultTask.getDuration());
                    resList.add(sourcemap);
                }
                LocalCache.set(pushDefaultTask.getDeviceId(), resList);
            }
            materialMapper.updateMaterialUsed(idsArr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.INSERT_DATA_ERROR);
        }

    }

    /**
     * 编辑默认任务
     *
     * @param pushDefaultTaskVo
     * @throws BusinessException
     */
    @Override
    public void editDefault(PushDefaultTaskVo pushDefaultTaskVo) throws BusinessException {
        try {
            User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
            String[] ids = pushDefaultTaskVo.getElevatorId().split(",");
            List<Equipment> equipmentList = this.equipmentMapper.getEquipmentListByIds(ids);
            Map<Integer, Equipment> map = listToMapOne(equipmentList);
            Equipment equipment = map.get(Integer.parseInt(pushDefaultTaskVo.getElevatorId()));
            PushDefaultTask defaultTask = new PushDefaultTask();
            defaultTask.setDeviceId(equipment.getDeviceId());
            defaultTask.setMaterial(pushDefaultTaskVo.getMaterial());
            defaultTask.setLocation(equipment.getLocation());
            defaultTask.setElevator(equipment.getId());
            defaultTask.setStatus(0);
            defaultTask.setDuration(pushDefaultTaskVo.getDuration());
            defaultTask.setUpdateUserName(String.valueOf(user.getId()));
            defaultTask.setId(Integer.parseInt(pushDefaultTaskVo.getId()));
            this.pushTaskMapper.editTaskDefault(defaultTask);
            //更新缓存默认任务
            //全部素材
            Map param = new HashMap();
            param.put("type", 0);
            param.put("userId", user.getId());
            List<Material> materialList = materialMapper.materialDictionary(param);
            //全部素材转成map数据
            Map materialMap = new HashMap();
            for (Material materialBean : materialList) {
                materialMap.put(String.valueOf(materialBean.getId()), materialBean);
            }
            //需要放到缓存池的素材集合
            List<Map> resList = new ArrayList<>();
            //素材id数组
            String[] materialId = defaultTask.getMaterial().split(",");
            //遍历id 查找素材
            List idsArr = new ArrayList();
            for (String id : materialId) {
                idsArr.add(id);
                Map sourcemap = new HashMap();
                Material material = (Material) materialMap.get(id);
                sourcemap.put("url", material.getUrl());
                sourcemap.put("type", material.getType());
                sourcemap.put("duration", defaultTask.getDuration());
                resList.add(sourcemap);
            }
            LocalCache.set(defaultTask.getDeviceId(), resList);
            materialMapper.updateMaterialUsed(idsArr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.INSERT_DATA_ERROR);
        }
    }

    public Map<Integer, Equipment> listToMapOne(List<Equipment> materialList) {
        Map<Integer, Equipment> map = materialList.stream()
                .collect(Collectors.toMap(Equipment::getId, Equipment -> Equipment));
        return map;
    }

}
