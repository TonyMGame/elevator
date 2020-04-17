package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class Login {

    @ApiModelProperty(notes = "授权码", required = true)
    private  String code;

}
