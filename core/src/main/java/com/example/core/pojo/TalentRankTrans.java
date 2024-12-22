package com.example.core.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TalentRankTrans implements Serializable {
    String name;
    String login;
    String gitId;
    String avatarUrl;
    String blo;
    String location;
    String locationCredence;
    String areas;
    String areaCredence;
    BigDecimal rank;
    List<RepositoryTrans> repositoryTrans;


}
