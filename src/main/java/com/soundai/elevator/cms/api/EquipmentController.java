package com.soundai.elevator.cms.api;


import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.service.EquipmentService;
import com.soundai.elevator.cms.vo.EquipmentPageVo;
import com.soundai.elevator.cms.vo.EquipmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@Api(tags = "设备管理")
@RestController
@RequestMapping("/equipment")
@CrossOrigin
public class EquipmentController {

    @Resource
    private EquipmentService equipmentService;

    @ApiOperation(value = "新增设备")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public void insert(@RequestBody EquipmentVo equipmentVo) throws BusinessException {
        this.equipmentService.insertEquipment(equipmentVo);
    }

    @ApiOperation(value = "设备列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Object list(@RequestBody EquipmentPageVo equipmentPageVo) throws BusinessException {
        return equipmentService.getEquipmentList(equipmentPageVo);
    }

    @ApiOperation(value = "删除设备")
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    public void list(@RequestParam int id) throws BusinessException {
        this.equipmentService.delEquipment(id);
    }

    @ApiOperation(value = "全部地点")
    @RequestMapping(value = "/location_dictionary", method = RequestMethod.GET)
    public Object getEquipmentLocationList() throws BusinessException {
       return this.equipmentService.getEquipmentLocationList();
    }

    @ApiOperation(value = "根据地点获取设备")
    @RequestMapping(value = "/elevator_dictionary", method = RequestMethod.GET)
    public Object getElevatorList(@RequestParam(required = false,value = "location") ArrayList<String> location) throws BusinessException {
        return this.equipmentService.getElevatorList(location);
    }

    @ApiOperation(value = "添加默认任务时的设备列表")
    @RequestMapping(value = "/add_def_ele_dina", method = RequestMethod.GET)
    public Object getDefElevatorList(@RequestParam(required = false,value = "location") ArrayList<String> location) throws BusinessException {
        return this.equipmentService.getDefElevatorList(location);
    }

}
