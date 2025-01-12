package com.example.dataanalysiscalculateservice.pojo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeveloperCollectionDTO {
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
    List<DeveloperProjectCollectionDTO> developerProjectCollectionList;

    public DeveloperCollectionDTO() {
        developerProjectCollectionList = new ArrayList<>();
    }



}
