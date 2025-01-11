package com.example.core.pojo.entity.mysql;

import com.example.core.pojo.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "project")
public class DeveloperProjectEntity extends BaseEntity {
    @Column(name = "git_id")
    private String gitId;
    @Column(name = "name")
    private String name;
    @Column(name = "language")
    private String language;
    @Column(name = "url")
    private String url;
    @Column(name = "forks_count")
    private Integer forksCount;
    @Column(name = "issues_count")
    private Integer issuesCount;
    @Column(name = "comments_count")
    private Integer commentsCount;
    @Column(name = "watchers_count")
    private Integer watchersCount;
    @Column(name = "stargazers_count")
    private Integer stargazersCount;
    @Column(name = "description")
    private String description;
    //todo


}
