package com.example.core.pojo.entity.mysql;

import com.example.core.enums.ProjectClassificationEnum;
import com.example.core.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "talent_rank_project")
public class TalentRankProjectEntity extends BaseEntity {
    String talentRankId;
    String name;
    String url;
    String description;
    Integer starCount;
    ProjectClassificationEnum classification;
}
