package com.soundai.elevator.cms.entity;

import lombok.Data;

@Data
public class User {

    private Integer id;

    private  String name;

    private  String companyName;

    private  Integer isDel;

    private  Integer level;

    private  String  accountId;

    private  String number;

    private  Long expireIn;

    private  String userId;

}
