package com.example.defaultvalue.model;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private Integer id;
    private String name;
    private List<Address> addresses;

    public User(Integer id) {
        this.id = id;
    }
}
