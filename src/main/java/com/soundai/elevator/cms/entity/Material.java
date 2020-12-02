package com.soundai.elevator.cms.entity;

import lombok.Data;

@Data
public class Material {

    private int id;

    private String name;

    private String url;

    private String materialId;

    private String poster;

    private int runNum;

    private int updateUser;

    private String updateUserName;

    private String md5;

    private int type;

    private int isUsed;


}
