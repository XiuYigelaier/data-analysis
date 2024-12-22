package com.example.core.pojo.entity.mysql;

import com.example.core.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "talent_rank")
public class TalentRankEntity extends BaseEntity {
    String name;
    String login;
    String gitId;
    String avatarUrl;
    String blo;
    String location;
    String locationCredence;
    String areas;
    String areaCredence;
    @Column(columnDefinition="decimal(18,2)")
    BigDecimal talentRank;
    String scoreHistory;


    public TalentRankEntity() {
        name = "无名氏";
    }
}
