package com.example.core.entity;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;


@Data
public class DeveloperNation  implements Serializable {
    private String name;
    private String location;
    private String company;
    private String pronouns;
    private String bio;


}
