package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class PullTaskVo {

    @ApiModelProperty(notes = "端deviceId ", required = true)
    private String deviceId;
    @ApiModelProperty(notes = "任务id ", required = true)
    private int taskId;
    @ApiModelProperty(notes = "有效素材id ", required = true)
    private List<Integer> materialIds;
    @ApiModelProperty(notes = "开始播放时间", required = true)
    private Date beginTime;
    @ApiModelProperty(notes = "上条任务最后播放时间 ", required = true)
    private Date lastRunTime;
    @ApiModelProperty(notes = "任务类型 ", required = true)
    private int type;
}
