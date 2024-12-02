package com.example.dataanalysisapiservice.service;

import com.example.dataanalysisapiservice.pojo.entity.TalentRank;

public interface ApiService {
    TalentRank calculateRank(String projectUser);
}
