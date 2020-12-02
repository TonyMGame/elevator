package com.soundai.elevator.cms.common;

import lombok.Data;

@Data
public class ProException extends RuntimeException{

    private int status;
    private String message;

    public ProException(int status,String message) {
        this.status =status;
        this.message =message;
    }

}
