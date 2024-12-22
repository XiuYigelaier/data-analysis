package com.example.core.pojo.entity.mysql;

import com.example.core.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "developer_and_project_relation_ship")
public class DeveloperAndProjectRelationShipEntity extends BaseEntity {
    private String developerId;
    private String projectId;
    private Integer commitCount;
    private  Integer issuesCommentEventCount;
    private  Integer pullRequestReviewEventCount;
    private  Boolean hasAnyRestrictedContributions;

    public DeveloperAndProjectRelationShipEntity() {
        commitCount = 0;
        issuesCommentEventCount = 0;
        pullRequestReviewEventCount = 0;
    }

    private  String  primaryLanguage;

}
