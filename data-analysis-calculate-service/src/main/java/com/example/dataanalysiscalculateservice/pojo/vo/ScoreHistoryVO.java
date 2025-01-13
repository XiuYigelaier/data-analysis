package com.example.dataanalysiscalculateservice.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScoreHistoryVO {
    @ApiModelProperty(value = "开发者gitID", example = "12345")
    String developerGitId;
    @ApiModelProperty(value = "开发者分数", example = "12345")
    BigDecimal score;
}
