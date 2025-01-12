package com.example.dataanalysiscalculateservice.pojo.vo;

import com.example.core.enums.ProjectClassificationEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TalentRankProjectVO {
    String developerId;
    String projectName;
    String url;
    Integer starCount;
    String description;
    BigDecimal score;
    ProjectClassificationEnum classification;
}
