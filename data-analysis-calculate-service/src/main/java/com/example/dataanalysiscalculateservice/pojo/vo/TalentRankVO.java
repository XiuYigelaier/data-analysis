package com.example.dataanalysiscalculateservice.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class TalentRankVO {
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
    List<BigDecimal> scoreHistory;
    List<TalentRankProjectVO> projectList;

    public TalentRankVO() {
        scoreHistory = new ArrayList<>();
        projectList = new ArrayList<>();
        rank = BigDecimal.ZERO;
    }
}
