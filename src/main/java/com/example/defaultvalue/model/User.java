package com.example.defaultvalue.model;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private String name;
    private Integer id;
    private List<Address> addresses;

    public User(String name) {
        this.name = name;
    }
}
