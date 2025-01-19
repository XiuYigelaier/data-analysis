package com.example.dataanalysiscollectionservice.pojo.vo.mysql;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeveloperProjectCollectionVO {
    @ApiModelProperty(value = "开发者id", required = true, example = "")
    private String developerId;
    @ApiModelProperty(value = "项目id", required = true, example = "")
    private String projectId;
    @ApiModelProperty(value = "项目提交数", required = true, example = "")
    private Integer commitCount;
    @ApiModelProperty(value = "事件提交数", required = true, example = "")
    private Integer issuesCommentEventCount;
    @ApiModelProperty(value = "审计书", required = true, example = "")
    private Integer pullRequestReviewEventCount;
    @ApiModelProperty(value = "是否有不可兼得贡献", required = true, example = "")
    private Boolean hasAnyRestrictedContributions;
    @ApiModelProperty(value = "项目gitId", required = true, example = "")
    private String gitId;
    @ApiModelProperty(value = "项目名称", required = true, example = "")
    private String name;
    @ApiModelProperty(value = "使用语言", required = true, example = "")
    private String language;
    @ApiModelProperty(value = "url", required = true, example = "")
    private String url;
    @ApiModelProperty(value = "事件数", required = true, example = "")
    private Integer issuesCount;
    @ApiModelProperty(value = "评论数", required = true, example = "")
    private Integer commentsCount;
    @ApiModelProperty(value = "关注人数", required = true, example = "")
    private Integer watchersCount;
    @ApiModelProperty(value = "star数量", required = true, example = "")
    private Integer stargazersCount;
    @ApiModelProperty(value = "项目描述", required = true, example = "")
    private String description;

}
