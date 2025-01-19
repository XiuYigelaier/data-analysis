package com.example.dataanalysiscollectionservice.pojo.po.mysql;

import com.example.core.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "collect_developer_collection")
public class DeveloperCollectionPO extends BaseEntity {
    @Column(name = "name", columnDefinition = "varchar(255) comment '开发者名称'")
    private String name;

    @Column(name = "login", columnDefinition = "varchar(255) comment '登录名'")
    private String login;

    @Column(name = "git_id", columnDefinition = "varchar(255) comment 'GitHub 用户ID'")
    private String gitId;

    @Column(name = "avatar_url", columnDefinition = "varchar(255) comment '头像 URL'")
    private String avatarUrl;

    @Column(name = "followers_count", columnDefinition = "int comment '粉丝数量'")
    private Integer followersCount;

    @Column(name = "public_repos_count", columnDefinition = "int comment '公共仓库数'")
    private Integer publicReposCount;

    @Column(name = "public_gists_count", columnDefinition = "int comment '公共代码片段数'")
    private Integer publicGistsCount;

    @Column(name = "developer_program_member_flag", columnDefinition = "bit(1) comment '是否是开发者计划成员标识'")
    private Boolean developerProgramMemberFlag;

    @Column(name = "campus_expert_flag", columnDefinition = "bit(1) comment '是否是校园专家标识'")
    private Boolean campusExpertFlag;

    @Column(name = "bounty_hunter_flag", columnDefinition = "bit(1) comment '是否是赏金猎人标识'")
    private Boolean bountyHunterFlag;

    @Column(name = "bio", columnDefinition = "text comment '简介/博客'")
    private String bio;

    @Column(name = "location", columnDefinition = "varchar(255) comment '所在位置'")
    private String location;

    @Column(name = "company", columnDefinition = "varchar(255) comment '公司名称'")
    private String company;

    @Column(name = "pronouns", columnDefinition = "varchar(255) comment '个人代称'")
    private String pronouns;

    @Column(name = "total_commit_contributions", columnDefinition = "int comment '总提交贡献数'")
    private Integer totalCommitContributions;

    @Column(name = "total_issue_contributions", columnDefinition = "int comment '总问题贡献数'")
    private Integer totalIssueContributions;

    @Column(name = "total_pull_request_contributions", columnDefinition = "int comment '总拉取请求贡献数'")
    private Integer totalPullRequestContributions;

    @Column(name = "total_repositories_with_commits", columnDefinition = "int comment '贡献过的仓库数量'")
    private Integer totalRepositoriesWithCommits;

    @Column(name = "has_any_restricted_contributions", columnDefinition = "bit(1) comment '是否有受限贡献'")
    private Boolean hasAnyRestrictedContributions;



}
