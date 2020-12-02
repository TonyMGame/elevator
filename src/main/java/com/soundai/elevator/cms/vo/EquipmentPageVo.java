package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel
public class EquipmentPageVo extends CommonPage {

    @ApiParam(hidden = true)
    @ApiModelProperty(notes = "userId 非必填 ")
    private int userId;

}
