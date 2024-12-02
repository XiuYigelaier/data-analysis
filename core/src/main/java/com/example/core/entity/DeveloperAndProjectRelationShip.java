package com.example.core.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class DeveloperAndProjectRelationShip extends BaseEntity {
    private String developerId;
    private String projectId;
    private Integer commitCount;
    private  Integer issuesCommentEventCount;
    private  Integer pullRequestReviewEventCount;
    private  Boolean hasAnyRestrictedContributions;

    public DeveloperAndProjectRelationShip() {
        commitCount = 0;
        issuesCommentEventCount = 0;
        pullRequestReviewEventCount = 0;
    }

    private  String  primaryLanguage;

}
