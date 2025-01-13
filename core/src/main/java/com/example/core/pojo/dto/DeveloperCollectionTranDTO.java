package com.example.core.pojo.dto;

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

    public DeveloperCollectionTranDTO() {
        developerProjectCollectionList = new ArrayList<>();
    }



}
