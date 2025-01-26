package com.example.dataanalysisapiservice.service;

import com.example.core.enums.ProjectClassificationEnum;
import com.example.dataanalysisapiservice.pojo.vo.ProjectClassificationVO;
import com.example.dataanalysisapiservice.pojo.vo.TalentRankVO;

import java.util.List;
import java.util.Map;

public interface ApiService {

    List<TalentRankVO> findAll();
    Map<ProjectClassificationEnum, Long> projectClassificationCount();

    List<ProjectClassificationVO> projectClassificationList();

}
