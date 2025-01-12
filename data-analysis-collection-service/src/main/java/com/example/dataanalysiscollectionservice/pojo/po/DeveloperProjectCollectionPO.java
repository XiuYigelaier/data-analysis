package com.example.dataanalysiscollectionservice.pojo.po;

import com.example.core.pojo.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "collect_developer_project_collection")
public class DeveloperProjectCollectionPO extends BaseEntity {
    @Column(name = "git_id",columnDefinition = "varchar(255) comment 'gitId'")
    private String gitId;
    @Column(name = "name",columnDefinition = "varchar(255) comment '项目名称'")
    private String name;
    @Column(name = "language",columnDefinition = "varchar(255) comment '语言'")
    private String language;
    @Column(name = "url",columnDefinition = "varchar(255) comment '项目url'")
    private String url;
    @Column(name = "issues_count",columnDefinition = "varchar(255) comment '事件数量'")
    private Integer issuesCount;
    @Column(name = "comments_count",columnDefinition = "varchar(255) comment '评论数量'")
    private Integer commentsCount;
    @Column(name = "watchers_count",columnDefinition = "varchar(255) comment '关注者数量'")
    private Integer watchersCount;
    @Column(name = "stargazers_count",columnDefinition = "varchar(255) comment '星标者数量'")
    private Integer stargazersCount;
    @Column(name = "description",columnDefinition = "varchar(255) comment '描述'")
    private String description;


}
