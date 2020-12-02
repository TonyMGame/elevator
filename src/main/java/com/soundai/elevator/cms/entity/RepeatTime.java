package com.soundai.elevator.cms.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RepeatTime {

    private long bgTime;

    private long edTime;

    private List<String> list;

}
