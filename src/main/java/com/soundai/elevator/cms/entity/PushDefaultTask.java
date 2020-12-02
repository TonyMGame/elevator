package com.soundai.elevator.cms.entity;

import lombok.Data;

@Data
public class PushDefaultTask {

    private int id;

    private String location;

    private int elevator;

    private String elevatorName;

    private String deviceId;

    private String material;

    private String materialName;

    private int status;

    private String updateUserName;

    private int duration;

}
