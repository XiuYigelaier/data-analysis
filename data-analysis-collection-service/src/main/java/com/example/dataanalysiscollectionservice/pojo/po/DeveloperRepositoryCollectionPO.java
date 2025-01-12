package com.example.dataanalysiscollectionservice.pojo.po;

import com.example.core.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "collect_developer_repository_collection")
public class DeveloperRepositoryCollectionPO extends BaseEntity {
    @Column(name = "developer_id",columnDefinition = "varchar(255) comment '开发者id'")
    String developerId;
    @Column(name = "repository_name",columnDefinition = "varchar(255) comment '仓库名'")
    String repositoryName;
    @Column(name = "url",columnDefinition = "varchar(255) comment '仓库地址")
    String url;
    @Column(name = "star_count",columnDefinition = "varchar(255) comment '星标数量'")
    Integer starCount;
}
