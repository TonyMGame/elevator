package com.soundai.elevator.cms.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PushDefaultTaskVo {

    @ApiModelProperty(notes = "id 编辑时必填")
    private String id;
    @ApiModelProperty(notes = "电梯设备id 编辑时必填")
    private String elevatorId;
    @ApiModelProperty(notes = "素材 编辑时必填")
    private String material;
    @ApiModelProperty(notes = "播放间隔 编辑时必填", dataType = "long")
    private int duration;
}
