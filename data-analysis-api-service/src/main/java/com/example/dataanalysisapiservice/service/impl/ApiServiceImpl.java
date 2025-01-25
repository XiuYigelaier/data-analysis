package com.example.dataanalysisapiservice.service.impl;

import com.example.core.pojo.base.ResponseModel;

import com.example.core.utils.RedisUtil;
import com.example.dataanalysisapiservice.feign.CalculateClientFeign;
import com.example.dataanalysisapiservice.pojo.dto.TalentRankDTO;
import com.example.dataanalysisapiservice.pojo.vo.TalentRankApIVO;
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
    public List<TalentRankApIVO> findAll() {
        List<TalentRankApIVO> talentRanks = redisUtil.getZSet("rank");
        if (!Collections.isEmpty(talentRanks)) {
            return talentRanks;
        }
        ResponseModel<List<TalentRankDTO>> responseModel = calculateClientFeign.findAll();
        List<TalentRankApIVO> result = new ArrayList<>();
        responseModel.getData().forEach(
                talentRankDTO -> {
                    TalentRankApIVO talentRankApiVO = new TalentRankApIVO();
                    BeanUtils.copyProperties(talentRankDTO, talentRankApiVO);
                    List<TalentRankApIVO.TalentRank_ProjectApiVO> talentRankProjectApiVOS = new ArrayList<>();
                    talentRankDTO.getProjectList().forEach(
                            projectDTO -> {
                                TalentRankApIVO.TalentRank_ProjectApiVO talentRankProjectApiVO = talentRankApiVO.new TalentRank_ProjectApiVO();
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
                talentRankApIVO -> {
                    redisUtil.addZSet("rank", talentRankApIVO, talentRankApIVO.getTalentRank().doubleValue());
                }
        );
        return redisUtil.getZSet("rank");

    }


}
