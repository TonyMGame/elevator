package com.soundai.elevator.cms.entity;


import lombok.Data;

@Data
public class Token {

    private  String accessToken;

    private  String refreshToken;

    private  long expireIn;


}
