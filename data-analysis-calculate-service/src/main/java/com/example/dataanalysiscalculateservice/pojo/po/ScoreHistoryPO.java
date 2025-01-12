package com.example.dataanalysiscalculateservice.pojo.po;

import com.example.core.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity(name = "cal_score_history")
@Data
public class ScoreHistoryPO extends BaseEntity {
    String developerGitId;
    BigDecimal score;
}
