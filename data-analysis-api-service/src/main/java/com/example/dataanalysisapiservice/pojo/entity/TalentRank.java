package com.example.dataanalysisapiservice.pojo.entity;

import com.example.core.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class TalentRank extends BaseEntity {
    String name;
    String gitId;
    String avatarUrl;
    String blo;
    String location;
    List<String> areas;
    BigDecimal rank;

}
