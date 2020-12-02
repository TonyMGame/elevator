package com.soundai.elevator.cms.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserPageVo extends CommonPage {

    @ApiModelProperty(notes = "用户账号")
    private String number;
    @ApiModelProperty(notes = "id",hidden = true)
    private Integer userId;

}
