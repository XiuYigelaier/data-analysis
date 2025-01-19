package com.example.dataanalysisapiservice.service;

import com.example.dataanalysisapiservice.pojo.dto.TalentRankDTO;
import com.example.dataanalysisapiservice.pojo.vo.TalentRankApiVO;

import java.util.List;

public interface ApiService {

     List<TalentRankApiVO> findAll();


}
