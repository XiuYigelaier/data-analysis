package com.example.dataanalysiscalculateservice.pojo.bo;

import lombok.Data;

@Data
public class CalculateDeveloperBO {
    Integer followersCount;
    Integer FlagCount;
    Integer reposCount;
    Integer gistCount;


    public CalculateDeveloperBO() {
        this.followersCount = 0;
        this.FlagCount = 0;
        this.reposCount = 0;
        this.gistCount = 0;
    }


}
