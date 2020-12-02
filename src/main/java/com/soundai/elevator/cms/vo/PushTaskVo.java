package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PushTaskVo {

    @ApiModelProperty(notes = "任务id", hidden = true)
    private int id;
    @ApiModelProperty(notes = "任务组taskId, 编辑时必填")
    private Long taskId;
    @ApiModelProperty(notes = "任务名称 编辑与添加时必填")
    private String name;
    @ApiModelProperty(notes = "电梯设备id 编辑与添加时必填")
    private String elevatorId;
    @ApiModelProperty(notes = "开始时间")
    private Long bgTime;
    @ApiModelProperty(notes = "结束时间")
    private Long edTime;
    @ApiModelProperty(notes = "播放间隔 编辑与添加时必填", dataType = "long")
    private int duration = 10;
    @ApiModelProperty(notes = "素材 编辑与添加时必填")
    private String material;
}
