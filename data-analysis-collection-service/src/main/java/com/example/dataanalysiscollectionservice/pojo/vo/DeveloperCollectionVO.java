package com.example.dataanalysiscollectionservice.pojo.vo;

import lombok.Data;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Data
public class DeveloperCollectionVO {
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
    String blo;
    String location;
    String company;
    String pronouns;
    Integer totalCommitContributions;
    Integer totalIssueContributions;
    Integer totalPullRequestContributions;
    Integer totalRepositoriesWithCommits;
    Boolean hasAnyRestrictedContributions;

    List<DeveloperProjectCollectionVO> developerProjectCollectionList;

    public DeveloperCollectionVO() {
        developerProjectCollectionList = new ArrayList<>();
    }
}
