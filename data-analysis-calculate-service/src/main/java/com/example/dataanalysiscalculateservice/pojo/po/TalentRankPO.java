package com.example.dataanalysiscalculateservice.pojo.po;

import com.example.core.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "cal_talent_rank")
public class TalentRankPO extends BaseEntity {
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
    String company;

    public TalentRankPO() {
        name = "无名氏";
    }
}
