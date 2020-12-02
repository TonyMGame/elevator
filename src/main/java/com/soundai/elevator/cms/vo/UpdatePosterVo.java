package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdatePosterVo {

    @ApiModelProperty(notes = "id", required = true)
    private int id;
    @ApiModelProperty(notes = "广告主", required = true)
    private String poster;

}
