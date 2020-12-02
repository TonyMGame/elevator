package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class EquipmentVo {

    @ApiModelProperty(notes = "设备id")
    private String equipmentId;
    @ApiModelProperty(notes = "电梯", required = true)
    private String elevator;
    @ApiModelProperty(notes = "所在位置", required = true)
    private String location;
    @ApiModelProperty(notes = "deviceSN", required = true)
    private String deviceSN;
    @ApiModelProperty(notes = "userId" )
    private int userId;

}
