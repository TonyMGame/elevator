package com.soundai.elevator.cms.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PushTask {

    private int id;

    private String name;

    private Date bgTime;

    private Date edTime;

    private String location;

    private String elevator;

    private String elevatorId;

    private String material;

    private String materialName;

    private String updateUserName;

    private String deviceId;

    private Long taskId;

    private int status;

    private int duration;


}
