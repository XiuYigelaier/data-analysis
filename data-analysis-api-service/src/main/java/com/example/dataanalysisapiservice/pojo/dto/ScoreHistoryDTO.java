package com.example.dataanalysisapiservice.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScoreHistoryDTO {
    @ApiModelProperty(value = "开发者gitID", example = "12345")
    String developerGitId;
    @ApiModelProperty(value = "开发者分数", example = "12345")
    BigDecimal score;
}
