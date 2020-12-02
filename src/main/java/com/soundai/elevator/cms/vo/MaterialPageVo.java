package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel
public class MaterialPageVo extends CommonPage{

    @ApiModelProperty(notes = "名称", required = true)
    private String name;
    @ApiModelProperty(notes = "广告主", required = true)
    private String poster;
    @ApiModelProperty(notes = "类型1图片 2视频", required = true)
    private int type;
    @ApiModelProperty(notes = "userId", hidden = true)
    private int userId;
    @ApiModelProperty(notes = "是否被使用，0 未使用，1使用")
    private String isUsed;

}
