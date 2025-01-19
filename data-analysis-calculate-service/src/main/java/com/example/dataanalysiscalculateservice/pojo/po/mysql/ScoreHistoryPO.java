package com.example.dataanalysiscalculateservice.pojo.po.mysql;

import com.example.core.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity(name = "cal_score_history")
@Data
public class ScoreHistoryPO extends BaseEntity {
    @Column(name = "developer_git_id")
    String developerGitId;
    @Column(name = "score")
    BigDecimal score;
}
