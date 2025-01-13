package com.example.dataanalysiscalculateservice.pojo.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CalculateRepoBO {
    @ApiModelProperty(value = "仓库的拉取请求审查数量", example = "5")
    private Integer pullReviewForReposCount;

    @ApiModelProperty(value = "仓库的提交数量", example = "100")
    private Integer commitForReposCount;

    @ApiModelProperty(value = "仓库的问题数量", example = "20")
    private Integer issuesForReposCount; // 注意修正了字段名以匹配其含义

    @ApiModelProperty(value = "仓库的总提交数量", example = "500")
    private Integer reposCommitsCount;

    @ApiModelProperty(value = "仓库的问题总数", example = "10")
    private Integer reposIssuesCount;

    @ApiModelProperty(value = "仓库的关注者数量", example = "30")
    private Integer reposWatcherCount;

    @ApiModelProperty(value = "仓库的星标数量", example = "50")
    private Integer reposStarCount;

    public CalculateRepoBO() {
        this.pullReviewForReposCount = 0;
        this.commitForReposCount = 0;
        this.issuesForReposCount = 0;
        this.reposCommitsCount = 0;
        this.reposIssuesCount = 0;
        this.reposWatcherCount = 0;
        this.reposStarCount  = 0;

    }
}
