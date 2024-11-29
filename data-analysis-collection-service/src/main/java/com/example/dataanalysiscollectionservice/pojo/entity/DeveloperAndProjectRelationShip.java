package com.example.dataanalysiscollectionservice.pojo.entity;

import com.example.core.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Base64;

@Entity
@Data
public class DeveloperAndProjectRelationShip extends BaseEntity {
    private String developerId;
    private String projectId;
    private Integer commitCount;
    private  Integer pushEventCount;
    private  Integer issuesCommentEventCount;
    private  Integer pullRequestReviewEventCount;
    private  Boolean hasAnyRestrictedContributions;
    private  String  primaryLanguage;
}
