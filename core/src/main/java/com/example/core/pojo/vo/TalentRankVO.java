package com.example.core.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Data
public class TalentRankVO {
    String name;
    String login;
    String gitId;
    String avatarUrl;
    String blo;
    String location;
    String locationCredence;
    String areas;
    String areaCredence;
    BigDecimal talentRank;
    List<TalentRankProjectVO> talentRankProjectVOList;
    List<Double> scoreHistory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TalentRankVO)) return false;
        TalentRankVO that = (TalentRankVO) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getLogin(), that.getLogin()) && Objects.equals(getGitId(), that.getGitId()) && Objects.equals(getAvatarUrl(), that.getAvatarUrl()) && Objects.equals(getBlo(), that.getBlo()) && Objects.equals(getLocation(), that.getLocation()) && Objects.equals(getLocationCredence(), that.getLocationCredence()) && Objects.equals(getAreas(), that.getAreas()) && Objects.equals(getAreaCredence(), that.getAreaCredence()) && Objects.equals(getTalentRank(), that.getTalentRank()) && Objects.equals(getTalentRankProjectVOList(), that.getTalentRankProjectVOList()) && Objects.equals(getScoreHistory(), that.getScoreHistory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLogin(), getGitId(), getAvatarUrl(), getBlo(), getLocation(), getLocationCredence(), getAreas(), getAreaCredence(), getTalentRank(), getTalentRankProjectVOList(), getScoreHistory());
    }
}
