package com.example.dataanalysiscalculateservice.pojo.bo;

import lombok.Data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "开发者统计信息业务对象")
public class CalculateDeveloperBO {

    @ApiModelProperty(value = "关注者数量", example = "100")
    private Integer followersCount;

    @ApiModelProperty(value = "标志数量（可能是指某种特定标志的计数，例如开发者计划成员标志等）", example = "1")
    private Integer flagCount; // 注意：这里已改为符合Java命名惯例的flagCount

    @ApiModelProperty(value = "仓库数量", example = "50")
    private Integer reposCount;

    @ApiModelProperty(value = "Gist数量", example = "20")
    private Integer gistCount;

    // 构造函数保持不变
    public CalculateDeveloperBO() {
        this.followersCount = 0;
        this.flagCount = 0;
        this.reposCount = 0;
        this.gistCount = 0;
    }


}
