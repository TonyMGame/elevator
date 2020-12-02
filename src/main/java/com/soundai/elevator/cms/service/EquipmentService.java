package com.soundai.elevator.cms.service;


import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.entity.Equipment;
import com.soundai.elevator.cms.vo.EquipmentPageVo;
import com.soundai.elevator.cms.vo.EquipmentVo;
import com.soundai.elevator.cms.vo.PageList;

import java.util.ArrayList;
import java.util.List;

public interface EquipmentService {

    PageList getEquipmentList(EquipmentPageVo equipmentPageVoVo) throws BusinessException;

    void insertEquipment(EquipmentVo equipmentVo) throws BusinessException;

    void delEquipment(int id) throws BusinessException;

    List<String> getEquipmentLocationList() throws BusinessException;

    List<Equipment> getElevatorList(ArrayList<String> location);

    List<Equipment> getDefElevatorList(ArrayList<String> location);

}
