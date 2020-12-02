package com.soundai.elevator.cms.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class EquipmentListByLocation {


    private int userId;

    private ArrayList<String> location;

}
