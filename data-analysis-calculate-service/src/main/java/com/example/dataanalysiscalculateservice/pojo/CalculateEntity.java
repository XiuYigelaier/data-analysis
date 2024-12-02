package com.example.dataanalysiscalculateservice.pojo;

import lombok.Data;

@Data
public class CalculateEntity {
    Integer followersCount;
    Integer FlagCount;
    Integer reposCount;
    Integer gistCount;
    Integer pullReviewForReposCount;
    Integer commitForReposCount;
    Integer issuesForRepsCount;
    Integer reposCommitsCount;
    Integer reposIssuesCount;
    Integer reposWatcherCounts;
    Integer reposStarCount;

    public CalculateEntity() {
        this.followersCount = 0;
        this.FlagCount = 0;
        this.reposCount = 0;
        this.gistCount = 0;
        this.pullReviewForReposCount = 0;
        this.commitForReposCount = 0;
        this.issuesForRepsCount = 0;
        this.reposCommitsCount = 0;
        this.reposIssuesCount = 0;
        this.reposWatcherCounts = 0;
        this.reposStarCount  = 0;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public CalculateEntity setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
        return this;

    }

    public Integer getFlagCount() {
        return FlagCount;
    }

    public CalculateEntity setFlagCount(Integer flagCount) {
        FlagCount = flagCount;
        return  this;
    }

    public Integer getReposCount() {
        return reposCount;
    }

    public CalculateEntity setReposCount(Integer reposCount) {
        this.reposCount = reposCount;
        return  this;
    }

    public Integer getGistCount() {
        return gistCount;
    }

    public CalculateEntity setGistCount(Integer gistCount) {
        this.gistCount = gistCount;
        return  this;
    }

    public Integer getPullReviewForReposCount() {
        return pullReviewForReposCount;
    }

    public CalculateEntity setPullReviewForReposCount(Integer pullReviewForReposCount) {
        this.pullReviewForReposCount = pullReviewForReposCount;
        return  this;
    }

    public Integer getCommitForReposCount() {
        return commitForReposCount;
    }

    public CalculateEntity setCommitForReposCount(Integer commitForReposCount) {
        this.commitForReposCount = commitForReposCount;
        return  this;
    }

    public Integer getIssuesForRepsCount() {
        return issuesForRepsCount;
    }

    public CalculateEntity setIssuesForRepsCount(Integer issuesForRepsCount) {
        this.issuesForRepsCount = issuesForRepsCount;
        return  this;
    }

    public Integer getReposCommitsCount() {
        return reposCommitsCount;
    }

    public CalculateEntity setReposCommitsCount(Integer reposCommitsCount) {
        this.reposCommitsCount = reposCommitsCount;
        return  this;
    }

    public Integer getReposIssuesCount() {
        return reposIssuesCount;
    }

    public CalculateEntity setReposIssuesCount(Integer reposIssuesCount) {
        this.reposIssuesCount = reposIssuesCount;
        return  this;
    }

    public Integer getReposWatcherCounts() {
        return reposWatcherCounts;
    }

    public CalculateEntity setReposWatcherCounts(Integer reposWatcherCounts) {
        this.reposWatcherCounts = reposWatcherCounts;
        return  this;
    }

    public Integer getReposStarCount() {
        return reposStarCount;
    }

    public CalculateEntity setReposStarCount(Integer reposStarCount) {
        this.reposStarCount = reposStarCount;
        return  this;
    }
}
