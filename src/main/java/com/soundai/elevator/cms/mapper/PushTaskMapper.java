package com.soundai.elevator.cms.mapper;

import com.soundai.elevator.cms.entity.ChangeDefaultStatus;
import com.soundai.elevator.cms.entity.PushDefaultTask;
import com.soundai.elevator.cms.entity.PushTask;
import com.soundai.elevator.cms.entity.RepeatTime;
import com.soundai.elevator.cms.vo.ChangeValueVo;
import com.soundai.elevator.cms.vo.DeletePushTask;
import com.soundai.elevator.cms.vo.PushTaskPageVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PushTaskMapper {

    /**
     * 查询任务列表
     *
     * @param pushTaskPageVo
     * @return
     */
    List<PushTask> getPushTaskList(PushTaskPageVo pushTaskPageVo);

    /**
     * 查询历史任务列表
     *
     * @param pushTaskPageVo
     * @return
     */
    List<PushTask> getPushTaskListH(PushTaskPageVo pushTaskPageVo);


    /**
     * 根据deviceId获取任务详情
     * @param taskId
     * @return
     */
    PushTask getPushTask(long taskId);

    /**
     * 添加任务
     *
     * @param list
     */
    void add(List<PushTask> list);

    /**
     * 更改任务状态
     *
     * @param changeValueVo
     */
    void changeStatus(ChangeValueVo changeValueVo);

    /**
     * 更改默认任务状态
     *
     * @param changeDefaultStatus
     */
    void changeDefaultStatus(ChangeDefaultStatus changeDefaultStatus);

    /**
     * 删除任务
     *
     * @param DeletePushTask
     */
    void deletePushTask(DeletePushTask DeletePushTask);

    /**
     * 删除任务
     * @param deviceId
     */
    void deletePushTaskByDeviceId(String deviceId);

    /**
     * 删除默认任务
     * @param deviceId
     */
    void deleteDefaultPushTaskByDeviceId(String deviceId);


    /**
     * 编辑任务
     *
     * @param list
     */
    void updateBatch(List<PushTask> list);

    /**
     * 需要执行的任务
     */
    List<Map> waitePushTask();

    /**
     * 需要结束的任务
     */
    List<Map> completePushTask();

    /**
     * 查询默认任务
     */
    List<Map> getDefaultTask();

    /**
     * 默认任务分页
     */
    List<PushDefaultTask> getPushTaskDefaultList(PushTaskPageVo pushTaskPageVo);

    /**
     * 添加任务
     *
     * @param list
     */
    void addTaskDefault(List<PushDefaultTask> list);

    /**
     * 编辑任务
     *
     * @param pushDefaultTask
     */
    void editTaskDefault(PushDefaultTask pushDefaultTask);

    /**
     * 获取被占用id
     *
     * @param taskId
     */
    List<Integer> getIdsByTaskId(Long taskId);

    /**
     * 删除批量
     *
     * @param taskId
     */
    void delBatch(Long taskId);

    /**
     * 验证是否有时间交叉任务
     *
     * @param repeatTime
     */
    int repeatTime(RepeatTime repeatTime);

    /**
     *
     * @param DeletePushTask
     */
    void updateTaskTime(DeletePushTask DeletePushTask);

}
