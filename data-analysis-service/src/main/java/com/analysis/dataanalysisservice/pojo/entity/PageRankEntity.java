package com.analysis.dataanalysisservice.pojo.entity;

import java.math.BigDecimal;

public class PageRankEntity {
    String fromName;
    String toName;
    BigDecimal weight;

    public PageRankEntity() {
    }

    public PageRankEntity(String fromName, String toName) {
        this.fromName = fromName;
        this.toName = toName;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
