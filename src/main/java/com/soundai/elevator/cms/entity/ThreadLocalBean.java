package com.soundai.elevator.cms.entity;

import lombok.Data;

@Data
public class ThreadLocalBean {

    private String token;

    public ThreadLocalBean(String token){
        this.token = token;
    }

}
