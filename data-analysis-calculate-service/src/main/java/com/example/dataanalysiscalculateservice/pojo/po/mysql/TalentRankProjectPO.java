package com.example.dataanalysiscalculateservice.pojo.po.mysql;

import com.example.dataanalysiscalculateservice.enums.ProjectClassificationEnum;
import com.example.core.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "cal_talent_rank_project")
public class TalentRankProjectPO extends BaseEntity {
    String developerId;
    String projectName;
    String url;
    @Column(name = "description", columnDefinition = "text comment '简介'")
    String description;
    Integer starCount;
    BigDecimal score;
    ProjectClassificationEnum classification;

    public TalentRankProjectPO() {
        score = BigDecimal.ZERO;
    }
}
