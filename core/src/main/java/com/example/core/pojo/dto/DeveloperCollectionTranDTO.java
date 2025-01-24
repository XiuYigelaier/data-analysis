package com.example.core.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DeveloperCollectionTranDTO implements Serializable {
    String name;
    String login;
    String gitId;
    String avatarUrl;
    Integer followersCount;
    Integer publicReposCount;
    Integer publicGistsCount;
    Boolean developerProgramMemberFlag;
    Boolean campusExpertFlag;
    Boolean bountyHunterFlag;
    String bio;
    String location;
    String company;
    String pronouns;
    Integer totalCommitContributions;
    Integer totalIssueContributions;
    Integer totalPullRequestContributions;
    Integer totalRepositoriesWithCommits;
    Boolean hasAnyRestrictedContributions;
    List<DeveloperProjectCollectionTranDTO> developerProjectCollectionList;


    @ApiModelProperty(value = "关注者数量")
    private Integer  followingCount;

    @ApiModelProperty(value = "开发者总提交数")
    private  Integer commitCount;

    @ApiModelProperty(value = "总评论数")
    private Integer  commentCount;

    @ApiModelProperty(value = "PR数")
    private Integer prCount;


    public DeveloperCollectionTranDTO() {
        developerProjectCollectionList = new ArrayList<>();
    }



}
