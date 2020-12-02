package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CommonPage {

    @ApiModelProperty(notes = "页码", required = true)
    private  int pageNum;
    @ApiModelProperty(notes = "条目", required = true)
    private  int pageSize;

}
