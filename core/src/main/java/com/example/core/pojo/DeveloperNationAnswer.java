package com.example.core.pojo;

import lombok.Data;

import java.io.Serializable;


@Data
public class DeveloperNationAnswer implements Serializable {
    private String name;
    private String location;
    private String company;
    private String pronouns;
    private String bio;


}
