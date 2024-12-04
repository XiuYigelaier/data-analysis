package com.example.core.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class Developer extends BaseEntity {
    @Column(name = "name")
    String name;
    @Column(name = "login")
    String login;
    @Column(name = "git_id")
    String gitId;
    @Column(name = "avatar_url")
    String avatarUrl;
    @Column(name = "followers_count")
    Integer followersCount;
    @Column(name = "public_repos_count")
    Integer publicReposCount;
    @Column(name = "public_gists_count")
    Integer publicGistsCount;
    @Column(name = "developer_program_member_flag")
    Boolean developerProgramMemberFlag;
    @Column(name = " campus_expert_flag")
    Boolean CampusExpertFlag;
    @Column(name = "bounty_hunter_flag")
    Boolean BountyHunterFlag;
    @Column(name = "blo")
    String blo;


}
