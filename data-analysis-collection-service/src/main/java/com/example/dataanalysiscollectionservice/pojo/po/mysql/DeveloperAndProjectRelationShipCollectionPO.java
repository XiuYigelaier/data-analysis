package com.example.dataanalysiscollectionservice.pojo.po.mysql;

import com.example.core.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "collect_developer_and_project_relationship_collection")
public class DeveloperAndProjectRelationShipCollectionPO extends BaseEntity {
    @Column(name = "developer_id",columnDefinition = "varchar(255) comment '开发者id'")
    private String developerId;
    @Column(name = "project_id",columnDefinition = "varchar(255) comment '项目id'")
    private String projectId;
    @Column(name = "commit_count",columnDefinition = "varchar(255) comment '提交数量'")
    private Integer commitCount;
    @Column(name = "issues_comment_event_count",columnDefinition = "varchar(255) comment '事件评论数'")
    private Integer issuesCommentEventCount;
    @Column(name = "pull_request_review_event_count",columnDefinition = "varchar(255) comment '审计数'")
    private Integer pullRequestReviewEventCount;
    @Column(name = "has_any_restricted_contributions",columnDefinition = "bit(1) comment '是否有不可见贡献'")
    private Boolean hasAnyRestrictedContributions;

    public DeveloperAndProjectRelationShipCollectionPO() {
        commitCount = 0;
        issuesCommentEventCount = 0;
        pullRequestReviewEventCount = 0;
    }

    private String primaryLanguage;

}
