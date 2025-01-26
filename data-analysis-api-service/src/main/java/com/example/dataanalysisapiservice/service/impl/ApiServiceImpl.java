package com.example.dataanalysisapiservice.service.impl;

import com.example.core.enums.ProjectClassificationEnum;
import com.example.core.pojo.base.ResponseModel;

import com.example.core.utils.RedisUtil;
import com.example.dataanalysisapiservice.feign.CalculateClientFeign;
import com.example.dataanalysisapiservice.mapper.ProjectClassificationMapper;
import com.example.dataanalysisapiservice.pojo.dto.ProjectClassificationDTO;
import com.example.dataanalysisapiservice.pojo.dto.TalentRankDTO;
import com.example.dataanalysisapiservice.pojo.vo.ProjectClassificationVO;
import com.example.dataanalysisapiservice.pojo.vo.TalentRankVO;
import com.example.dataanalysisapiservice.pojo.vo.TalentRank_ProjectVO;
import com.example.dataanalysisapiservice.service.ApiService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    CalculateClientFeign calculateClientFeign;


    @Override
    public List<TalentRankVO> findAll() {
        List<TalentRankVO> talentRanks = redisUtil.getZSet("rank");
        if (!Collections.isEmpty(talentRanks)) {
            return talentRanks;
        }
        ResponseModel<List<TalentRankDTO>> responseModel = calculateClientFeign.findAll();
        List<TalentRankVO> result = new ArrayList<>();
        responseModel.getData().forEach(
                talentRankDTO -> {
                    TalentRankVO talentRankVO = new TalentRankVO();
                    BeanUtils.copyProperties(talentRankDTO, talentRankVO);
                    List<TalentRank_ProjectVO> talentRankProjectApiVOS = new ArrayList<>();
                    talentRankDTO.getProjectList().forEach(
                            projectDTO -> {
                                TalentRank_ProjectVO talentRankProjectApiVO = new TalentRank_ProjectVO();
                                BeanUtils.copyProperties(projectDTO, talentRankProjectApiVO);
                                talentRankProjectApiVOS.add(talentRankProjectApiVO);
                            }
                    );
                    talentRankVO.setProjectList(talentRankProjectApiVOS);
                    List<BigDecimal> scoreHistoryApiVOS = new ArrayList<>(talentRankDTO.getScoreHistory());
                    talentRankVO.setScoreHistory(scoreHistoryApiVOS);

                    result.add(talentRankVO);
                }
        );

        result.forEach(
                talentRankVO -> {
                    redisUtil.addZSet("rank", talentRankVO, talentRankVO.getTalentRank().doubleValue());
                }
        );
        return redisUtil.getZSet("rank");

    }

    public  Map<ProjectClassificationEnum,Long> projectClassificationCount(){
        return  calculateClientFeign.projectClassificationCount().getData();
    }

    public  List<ProjectClassificationVO> projectClassificationList(){
        ProjectClassificationMapper projectClassificationMapper = ProjectClassificationMapper.INSTANCE;
       ResponseModel<List<ProjectClassificationDTO>>  responseModel = calculateClientFeign.projectClassificationList();
       return projectClassificationMapper.DTOtoVOList(responseModel.getData());

    }


}
