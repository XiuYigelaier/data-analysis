package com.example.dataanalysisapiservice.service.impl;

import com.example.core.entity.ResponseModel;
import com.example.core.utils.RedisUtil;
import com.example.dataanalysisapiservice.feign.CollectionClientFeign;
import com.example.dataanalysisapiservice.pojo.entity.TalentRank;
import com.example.dataanalysisapiservice.repository.TalentRankRepository;
import com.example.dataanalysisapiservice.service.ApiService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    CollectionClientFeign collectionClientFeign;
    @Autowired
    TalentRankRepository talentRankRepository;
    @Override
    public TalentRank calculateRank(String projectUser) {
        ResponseModel<?> responseModel = collectionClientFeign.calcuate(projectUser);
        if(!responseModel.isSuccess()){
            throw new RuntimeException(responseModel.getMessageInfo());
        }
        LinkedHashMap data = (LinkedHashMap) responseModel.getData();
        TalentRank talentRank = new TalentRank();
        Optional<TalentRank> talentRankOpt = talentRankRepository.findByGitIdAndDeletedFalse((String) data.get("gitId"));
        if(talentRankOpt.isPresent()){
            talentRank = talentRankOpt.get();

        }
        talentRank.setTalentRank(BigDecimal.valueOf((Double) data.get("rank")));
        talentRank.setName((String) data.get("name"));
        talentRank.setGitId((String) data.get("gitId"));
        talentRank.setBlo((String) data.get("blo"));
        talentRank.setLocation((String) data.get("location"));
        talentRank.setAvatarUrl((String) data.get("avatarUrl"));
        talentRank.setAreas((String)data.get("areas"));
        talentRank.setAreaCredence((String)data.get("areaCredence"));
        talentRank.setLocationCredence((String)data.get("locationCredence"));
        talentRank.setLocation((String)data.get("location"));
        talentRank.setLogin((String)data.get("login"));
       // talentRank.setAreas((List<String>) data.get("areas"));
       talentRankRepository.save(talentRank);
       redisUtil.addZSet("rank",talentRank,talentRank.getTalentRank().doubleValue());
        return talentRank;
    }

    @Override
    public List<TalentRank> findAll() {
        List<TalentRank> talentRanks = redisUtil.getZSet("rank");
        if(!Collections.isEmpty(talentRanks)){
            return  talentRanks;
        }
        List<TalentRank> talentRankList = talentRankRepository.findAllByOrderByTalentRankDesc().get();
        for(TalentRank talentRank:talentRankList){
            redisUtil.addZSet("rank",talentRank,talentRank.getTalentRank().doubleValue());
        }
        return  talentRankList;

    }


}
