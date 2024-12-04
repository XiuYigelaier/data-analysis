package com.example.dataanalysisapiservice.service;

import com.example.dataanalysisapiservice.pojo.entity.TalentRank;

import java.util.List;

public interface ApiService {
    TalentRank calculateRank(String projectUser);

     List<TalentRank> findAll();
}
