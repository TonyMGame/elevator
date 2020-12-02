package com.soundai.elevator.cms.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class PageList extends CommonPage{

    private long total;

    private int pageTotal;

    private Object obj;

    public PageList(int pageNum, int pageSize, long total, int pageTotal, Object obj){
        super.setPageNum(pageNum);
        super.setPageSize(pageSize);
        this.obj=obj;
        this.total=total;
        this.pageTotal=pageTotal;
    }

}
