package com.soundai.elevator.cms.service;

import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.entity.ChangeDefaultStatus;
import com.soundai.elevator.cms.vo.*;


public interface PushTaskService {
    PageList getPushTaskList(PushTaskPageVo pushTaskPageVo) throws BusinessException;

    PageList getHistoryPushTaskList (PushTaskPageVo pushTaskPageVo) throws BusinessException;

    void add(PushTaskVo pushTaskVo) throws BusinessException;

    void edit(PushTaskVo pushTaskVo) throws BusinessException;

    void changeStatus(ChangeValueVo changeValueVo) throws BusinessException;

    void changeDefaultStatus(ChangeDefaultStatus changeDefaultStatus) throws BusinessException;

    void deleteStatus(DeletePushTask DeletePushTask) throws BusinessException;

    PageList getPushTaskDefaultList(PushTaskPageVo pushTaskPageVo) throws BusinessException;

    void addDefault(PushDefaultTaskVo pushDefaultTaskVo) throws BusinessException;

    void editDefault(PushDefaultTaskVo pushDefaultTaskVo) throws BusinessException;

}
