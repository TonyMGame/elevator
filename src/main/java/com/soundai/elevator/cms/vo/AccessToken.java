package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel
@Data
public class AccessToken {

    @ApiModelProperty(notes = "accessToken")
    private  String  accessToken;

}
