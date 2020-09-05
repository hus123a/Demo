package com.catCoder.bean;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ObUser {
    private  String name;

    private int age;

    private int sex;

    private String hobby;
}
