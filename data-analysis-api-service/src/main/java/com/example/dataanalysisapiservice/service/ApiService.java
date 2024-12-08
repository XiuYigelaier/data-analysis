package com.example.dataanalysisapiservice.service;



import com.example.core.entity.TalentRank;

import java.util.List;

public interface ApiService {
    TalentRank calculateRank(String projectUser,Boolean updateFlag);

     List<TalentRank> findAll();
}
