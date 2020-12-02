package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel
@Data
public class AccessToken {

    @ApiModelProperty(notes = "accessToken")
    private String accessToken;

    @ApiModelProperty(notes = "userId", hidden = true)
    private Integer userId;

    @ApiModelProperty(notes = "name", hidden = true)
    private String name;

    @ApiModelProperty(notes = "phone", hidden = true)
    private String phone;

}
