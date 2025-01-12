package com.example.dataanalysiscalculateservice.pojo.bo;

import lombok.Data;

import java.io.Serializable;


@Data
public class DeveloperNationAnswerBO implements Serializable {
    private String name;
    private String location;
    private String company;
    private String pronouns;
    private String bio;


}
