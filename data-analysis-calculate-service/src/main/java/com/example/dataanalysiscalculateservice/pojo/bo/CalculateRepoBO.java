package com.example.dataanalysiscalculateservice.pojo.bo;

import lombok.Data;

@Data
public class CalculateRepoBO {
    Integer pullReviewForReposCount;
    Integer commitForReposCount;
    Integer issuesForRepsCount;
    Integer reposCommitsCount;
    Integer reposIssuesCount;
    Integer reposWatcherCount;
    Integer reposStarCount;

    public CalculateRepoBO() {
        this.pullReviewForReposCount = 0;
        this.commitForReposCount = 0;
        this.issuesForRepsCount = 0;
        this.reposCommitsCount = 0;
        this.reposIssuesCount = 0;
        this.reposWatcherCount = 0;
        this.reposStarCount  = 0;

    }
}
