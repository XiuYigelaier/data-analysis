package com.example.core.pojo.base;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUser implements UserDetails {
    private UserPO userPO;

    public LoginUser(UserPO userPO) {
        this.userPO = userPO;
    }

    public UserPO getUser() {
        return userPO;
    }

    public void setUser(UserPO userPO) {
        this.userPO = userPO;
    }


    public LoginUser() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userPO.getPassword();
    }

    @Override
    public String getUsername() {
        return userPO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
