package com.soundai.elevator.cms.service;

import com.soundai.elevator.cms.common.BusinessException;

import java.util.List;
import java.util.Map;

public interface DirectivePush {

    void pushMassage(String deviceId, List<Map<String,Object>> sources) throws BusinessException;

}
