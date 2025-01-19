package com.example.dataanalysisapiservice.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TalentRankProjectApiVO {

    @ApiModelProperty(value = "开发者ID", example = "12345")
    private String developerId;

    @ApiModelProperty(value = "项目名称", example = "My Awesome Project")
    private String projectName;

    @ApiModelProperty(value = "项目URL（例如GitHub仓库链接）", example = "https://github.com/user/repo")
    private String url;

    @ApiModelProperty(value = "星标数量（表示项目的受欢迎程度）", example = "42")
    private Integer starCount;

    @ApiModelProperty(value = "项目描述", example = "这是一个非常棒的项目，用于解决...")
    private String description;

    @ApiModelProperty(value = "项目评分（数值越大表示评分越高）", example = "9.5")
    private BigDecimal score;

    @ApiModelProperty(value = "项目分类", notes = "这是一个枚举类型，表示项目的分类，例如'开源'、'商业'等")
    private String classification;


}


