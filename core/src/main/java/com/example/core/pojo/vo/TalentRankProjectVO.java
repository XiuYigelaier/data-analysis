package com.example.core.pojo.vo;

import lombok.Data;

import java.util.Objects;

@Data
public class TalentRankProjectVO {
    String repositoryName;
    String url;
    Integer starCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TalentRankProjectVO)) return false;
        TalentRankProjectVO that = (TalentRankProjectVO) o;
        return Objects.equals(getRepositoryName(), that.getRepositoryName()) && Objects.equals(getUrl(), that.getUrl()) && Objects.equals(getStarCount(), that.getStarCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRepositoryName(), getUrl(), getStarCount());
    }
}
