package com.example.core.pojo.entity.mysql;

import com.example.core.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "repository")
public class DeveloperRepositoryEntity extends BaseEntity {
    String developerId;
    String repositoryName;
    String url;
    Integer starCount;
}
