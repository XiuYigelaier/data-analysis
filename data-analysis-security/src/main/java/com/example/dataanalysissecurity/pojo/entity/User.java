package com.example.dataanalysissecurity.pojo.entity;

import com.example.core.entity.BaseEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Collection;

@Entity
@Data
public class User extends BaseEntity {
    @Column(name = "username",unique = true)
    private String userName;


    @Column(name = "password")
    private String password;


}
