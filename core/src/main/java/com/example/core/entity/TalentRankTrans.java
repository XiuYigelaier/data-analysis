package com.example.core.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TalentRankTrans  {
    String name;
    String gitId;
    String avatarUrl;
    String blo;
    String location;
    String locationCredence;
    String areas;
    String areaCredence;
    BigDecimal rank;


}
