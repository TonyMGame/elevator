package com.soundai.elevator.cms.service;

import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.vo.AccessToken;

public interface UserService {

    AccessToken getAccessToken(String code) throws BusinessException;

}
