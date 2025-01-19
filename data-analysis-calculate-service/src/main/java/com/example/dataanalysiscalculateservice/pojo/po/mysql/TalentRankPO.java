package com.example.dataanalysiscalculateservice.pojo.po.mysql;

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

    @Column(name = "name")
    private String name;
    @Column(name = "login")
    private String login;
    @Column(name = "git_id")
    private String gitId;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "bio")
    private String bio;
    @Column(name = "location")
    private String location;
    @Column(name = "location_credence")
    private String locationCredence;
    @Column(name = "areas")
    private String areas;
    @Column(name = "area_credence")
    private String areaCredence;

    @Column(name = "talent_rank", columnDefinition = "decimal(18,2)")
    private BigDecimal talentRank;

    @Column(name = "company")
    private String company;

    public TalentRankPO() {
        name = "无名氏";
    }
}
