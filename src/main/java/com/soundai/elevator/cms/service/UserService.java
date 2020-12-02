package com.soundai.elevator.cms.service;

import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.vo.ChangeValueVo;
import com.soundai.elevator.cms.vo.PageList;
import com.soundai.elevator.cms.vo.UserPageVo;

public interface UserService {

    PageList getUserList(UserPageVo userVo) throws BusinessException;

    void delUser(int id) throws BusinessException;

    void upLevel(ChangeValueVo changeValueVo) throws BusinessException;

    void updateCompanyName(ChangeValueVo changeValueVo) throws BusinessException;

    void updateName(ChangeValueVo changeValueVo) throws BusinessException;

}
