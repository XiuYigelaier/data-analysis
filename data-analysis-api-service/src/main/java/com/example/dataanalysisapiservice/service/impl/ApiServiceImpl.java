package com.example.dataanalysisapiservice.service.impl;

import com.example.core.pojo.base.ResponseModel;

import com.example.core.utils.RedisUtil;
import com.example.dataanalysisapiservice.feign.CalculateClientFeign;
import com.example.dataanalysisapiservice.pojo.dto.TalentRankDTO;
import com.example.dataanalysisapiservice.pojo.vo.TalentRankApiVO;
import com.example.dataanalysisapiservice.pojo.vo.TalentRankProjectApiVO;
import com.example.dataanalysisapiservice.service.ApiService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    CalculateClientFeign calculateClientFeign;


    @Override
    public List<TalentRankApiVO> findAll() {
        List<TalentRankApiVO> talentRanks = redisUtil.getZSet("rank");
        if (!Collections.isEmpty(talentRanks)) {
            return talentRanks;
        }
        ResponseModel<List<TalentRankDTO>> responseModel = calculateClientFeign.findAll();
        List<TalentRankApiVO> result = new ArrayList<>();
        responseModel.getData().forEach(
                talentRankDTO -> {
                    TalentRankApiVO talentRankApiVO = new TalentRankApiVO();
                    BeanUtils.copyProperties(talentRankDTO, talentRankApiVO);
                    List<TalentRankProjectApiVO> talentRankProjectApiVOS = new ArrayList<>();
                    talentRankDTO.getProjectList().forEach(
                            projectDTO -> {
                                TalentRankProjectApiVO talentRankProjectApiVO = new TalentRankProjectApiVO();
                                BeanUtils.copyProperties(projectDTO, talentRankProjectApiVO);
                                talentRankProjectApiVOS.add(talentRankProjectApiVO);
                            }
                    );
                    talentRankApiVO.setProjectList(talentRankProjectApiVOS);
                    List<BigDecimal> scoreHistoryApiVOS = new ArrayList<>(talentRankDTO.getScoreHistory());
                    talentRankApiVO.setScoreHistory(scoreHistoryApiVOS);

                    result.add(talentRankApiVO);
                }
        );

        result.forEach(
                talentRankApiVO -> {
                    redisUtil.addZSet("rank", talentRankApiVO, talentRankApiVO.getTalentRank().doubleValue());
                }
        );
        return redisUtil.getZSet("rank");

    }


}
