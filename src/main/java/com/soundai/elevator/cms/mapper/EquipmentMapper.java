package com.soundai.elevator.cms.mapper;

import com.soundai.elevator.cms.entity.Equipment;
import com.soundai.elevator.cms.entity.EquipmentListByLocation;
import com.soundai.elevator.cms.entity.Material;
import com.soundai.elevator.cms.vo.EquipmentPageVo;
import com.soundai.elevator.cms.vo.EquipmentVo;
import com.soundai.elevator.cms.vo.MaterialPageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface EquipmentMapper {

    /**
     * 插入设备
     * @param equipmentVo
     */
    void insertEquipment(EquipmentVo equipmentVo);

    /**
     * 根据组id获取设备列表
     * @param equipmentPageVo
     * @return
     */
    List<Equipment> getEquipmentList(EquipmentPageVo equipmentPageVo);

    /**
     * 删除设备
     * @param id
     */
    void delEquipment(int id);

    /**
     * 查询设备含所的全部地点
     * @param
     */
    List<String> getEquipmentLocationList(String userId);

    /**
     * 根据位置获取设备列表
     * @param equipmentListByLocation
     * @return
     */
    List<Equipment> getEquipmentListByLocation(EquipmentListByLocation equipmentListByLocation);

    /**
     * 根据位置获取设备列表 排除已经使用的
     * @param equipmentListByLocation
     * @return
     */
    List<Equipment> getDefEquipmentListByLocation(EquipmentListByLocation equipmentListByLocation);

    /**
     * 根据ids获取设备
     * @param ids
     * @return
     */
    List<Equipment> getEquipmentListByIds(String[] ids);

    /**
     * 查询重复sn
     * @param sn
     * @return
     */
    int getEquipmentBySn(String sn);

    /**
     * 根据查询设备
     * @param id
     * @return
     */
    Equipment getEquipment(int id);

}
