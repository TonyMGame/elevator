package com.soundai.elevator.cms.service;

import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.vo.AccessToken;
import javax.servlet.http.HttpServletResponse;

public interface TokenService {

    AccessToken getAccessToken(String code) throws BusinessException;

    void logout(HttpServletResponse response, String token);

}
