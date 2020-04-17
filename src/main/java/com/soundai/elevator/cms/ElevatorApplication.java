package com.soundai.elevator.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude={
        SecurityAutoConfiguration.class
})
public class ElevatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElevatorApplication.class, args);
    }

}
