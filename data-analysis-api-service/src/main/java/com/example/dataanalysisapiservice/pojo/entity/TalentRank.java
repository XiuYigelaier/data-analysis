package com.example.dataanalysisapiservice.pojo.entity;

import com.example.core.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Data
@Entity
public class TalentRank extends BaseEntity {
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

}
