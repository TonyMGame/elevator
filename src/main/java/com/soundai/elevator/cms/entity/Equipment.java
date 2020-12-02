package com.soundai.elevator.cms.entity;

import lombok.Data;

@Data
public class Equipment {

    private int id;

    private String deviceId;

    private String elevator;

    private String location;

    private int group;

}
