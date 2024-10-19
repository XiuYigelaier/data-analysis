package com.example.core.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class User extends BaseEntity {
    @Column(name = "username",unique = true)
    private String userName;


    @Column(name = "password")
    private String password;


}
