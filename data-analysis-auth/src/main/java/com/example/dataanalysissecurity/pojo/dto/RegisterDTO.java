package com.example.dataanalysissecurity.pojo.dto;

import com.sun.istack.NotNull;
import lombok.Data;


public class RegisterDTO {
    @NotNull
    private String userName;
    @NotNull
    private String password;

    public RegisterDTO() {
    }

    public RegisterDTO(@NotNull String userName, @NotNull String password) {
        this.userName = userName;
        this.password = password;
    }

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
