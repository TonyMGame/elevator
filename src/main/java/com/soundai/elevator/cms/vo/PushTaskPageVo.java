package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class PushTaskPageVo extends CommonPage {

    @ApiModelProperty(notes = "任务名称")
    private String name;
    @ApiModelProperty(notes = "开始时间")
    private Long bgTime;
    @ApiModelProperty(notes = "结束时间")
    private Long edTime;
    @ApiModelProperty(notes = "位置",hidden = true)
    private String location;
    @ApiModelProperty(notes = "电梯")
    private String elevator;
    @ApiModelProperty(notes = "电梯名称")
    private String elevatorName;
    @ApiModelProperty(notes = "素材")
    private String material;
    @ApiModelProperty(notes = "设备Id")
    private int equipmentId;
    @ApiModelProperty(notes = "设备deviceId")
    private String deviceId;
    @ApiModelProperty(notes = "状态")
    private int status;
    @ApiModelProperty(notes = "userId",hidden = true)
    private int userId;

}
