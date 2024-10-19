package com.example.dataanalysissecurity.pojo.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginDTO {
    @NotNull
    private String userName;
    @NotNull
    private String password;

    public LoginDTO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public LoginDTO() {
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
