package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DeletePushTask {

    @ApiModelProperty(notes = "任务组taskId", required = true)
    private  long taskId;

}
