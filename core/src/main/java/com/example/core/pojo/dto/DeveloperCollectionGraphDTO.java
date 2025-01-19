package com.example.core.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DeveloperCollectionGraphDTO {


    private String gitId;

    private String avatarUrl;

    private String login;

    private String name;

    private BigDecimal score;
    private List<DeveloperCollectionGraphDTO> followings;

    private List<DeveloperCollectionGraphDTO> followers;

    public DeveloperCollectionGraphDTO() {
        followers = new ArrayList<>();
        followings = new ArrayList<>();

    }
}

