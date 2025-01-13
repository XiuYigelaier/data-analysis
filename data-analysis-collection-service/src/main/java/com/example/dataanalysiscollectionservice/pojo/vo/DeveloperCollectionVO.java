package com.example.dataanalysiscollectionservice.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Data
public class DeveloperCollectionVO {
    @ApiModelProperty(value = "用户名", example = "john_doe")
    private String name;

    @ApiModelProperty(value = "登录名", example = "johndoe123")
    private String login;

    @ApiModelProperty(value = "GitHub ID", example = "1234567")
    private String gitId;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.png")
    private String avatarUrl;

    @ApiModelProperty(value = "关注者数量")
    private Integer followersCount;

    @ApiModelProperty(value = "公共仓库数量")
    private Integer publicReposCount;

    @ApiModelProperty(value = "公共Gist数量")
    private Integer publicGistsCount;

    @ApiModelProperty(value = "开发者计划成员标志")
    private Boolean developerProgramMemberFlag;

    @ApiModelProperty(value = "校园专家标志")
    private Boolean campusExpertFlag;

    @ApiModelProperty(value = "赏金猎人标志")
    private Boolean bountyHunterFlag;

    // 注意：'bio'字段可能是一个拼写错误，这里保留原样，但建议检查其实际意义
    @ApiModelProperty(value = "博客'", example = "someValue")
    private String bio;

    @ApiModelProperty(value = "位置", example = "San Francisco, CA")
    private String location;

    @ApiModelProperty(value = "公司", example = "GitHub Inc.")
    private String company;

    @ApiModelProperty(value = "代词", example = "he/him")
    private String pronouns;

    @ApiModelProperty(value = "总提交贡献数")
    private Integer totalCommitContributions;

    @ApiModelProperty(value = "总Issue贡献数")
    private Integer totalIssueContributions;

    @ApiModelProperty(value = "总Pull Request贡献数")
    private Integer totalPullRequestContributions;

    @ApiModelProperty(value = "有提交的总仓库数")
    private Integer totalRepositoriesWithCommits;

    @ApiModelProperty(value = "是否有任何受限贡献")
    private Boolean hasAnyRestrictedContributions;

    @ApiModelProperty(value = "开发者项目集合列表")
    private List<DeveloperProjectCollectionVO> developerProjectCollectionList;



    public DeveloperCollectionVO() {
        developerProjectCollectionList = new ArrayList<>();
    }
}
