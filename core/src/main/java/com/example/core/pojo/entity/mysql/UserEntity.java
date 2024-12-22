package com.example.core.pojo.entity.mysql;

import com.example.core.pojo.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEntity extends BaseEntity {
    @Column(name = "username",unique = true)
    private String userName;


    @Column(name = "password")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
