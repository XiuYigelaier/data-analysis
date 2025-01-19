package com.example.dataanalysisapiservice.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class TalentRankDTO {
    @ApiModelProperty(value = "开发者名", example = "john_doe")
    private String name;

    @ApiModelProperty(value = "登录名", example = "john_doe")
    private String login;

    @ApiModelProperty(value = "GitID（可能是GitHub或其他Git服务平台的唯一标识符）", example = "12345678")
    private String gitId;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.png")
    private String avatarUrl;

    @ApiModelProperty(value = "博客URL（注意：这里字段名'bio'可能是'biog'的简写或误打，建议检查并修正）", example = "https://biog.example.com")
    private String bio; // 建议修正为 private String biog;

    @ApiModelProperty(value = "所在地", example = "San Francisco, CA")
    private String location;

    @ApiModelProperty(value = "位置可信度", example = "high")
    private String locationCredence;

    @ApiModelProperty(value = "擅长领域", example = "Java, Spring")
    private String areas;

    @ApiModelProperty(value = "领域可信度", example = "medium")
    private String areaCredence;

    @ApiModelProperty(value = "分数", example = "5.7")
    private BigDecimal talentRank;

    @ApiModelProperty(value = "历史分数记录（列表中的每个元素代表一个历史分数）", notes = "列表中的元素类型为BigDecimal")
    private List<BigDecimal> scoreHistory;

    @ApiModelProperty(value = "参与的项目列表", notes = "列表中的每个元素是TalentRankProjectVO类型，代表开发者参与的一个项目")
    private List<TalentRankProjectDTO> projectList;

    public TalentRankDTO() {
        scoreHistory = new ArrayList<>();
        projectList = new ArrayList<>();
        talentRank = BigDecimal.ZERO;
    }
}
