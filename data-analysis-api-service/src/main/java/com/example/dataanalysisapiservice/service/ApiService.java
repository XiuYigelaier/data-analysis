package com.example.dataanalysisapiservice.service;



import com.example.core.pojo.entity.mysql.TalentRankEntity;

import java.util.List;

public interface ApiService {
    TalentRankVO calculateRank(String projectUser, Boolean updateFlag);

     List<TalentRankVO> findAll();

    void pageRank();
}
