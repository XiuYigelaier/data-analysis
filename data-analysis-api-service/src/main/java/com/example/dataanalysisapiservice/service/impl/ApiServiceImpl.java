package com.example.dataanalysisapiservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.core.pojo.base.ResponseModel;
import com.example.core.pojo.entity.mysql.TalentRankByPageRankEntity;
import com.example.core.pojo.entity.mysql.TalentRankEntity;
import com.example.core.pojo.entity.mysql.TalentRankProjectEntity;
import com.example.core.repository.mysql.TalentRankByPageRankRepository;
import com.example.core.repository.mysql.TalentRankProjectRepository;
import com.example.core.utils.RedisUtil;
import com.example.dataanalysisapiservice.feign.CollectionClientFeign;
import com.example.core.repository.mysql.TalentRankRepository;
import com.example.dataanalysisapiservice.service.ApiService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    TalentRankByPageRankRepository talentRankByPageRankRepository;
    @Autowired
    CollectionClientFeign collectionClientFeign;
    @Autowired
    TalentRankRepository talentRankRepository;
    @Autowired
    TalentRankProjectRepository talentRankProjectRepository;
    @Override
    public TalentRankVO calculateRank(String login, Boolean updateFlag) {
        Optional<TalentRankEntity> ans = talentRankRepository.findByLoginAndDeletedFalse(login);
        //不更新
//        if(ans.isPresent()&&!updateFlag){
//            return ans.get();
//        }
        //更新 先redis移除
        if(ans.isPresent()){
            TalentRankEntity redisEntity =  ans.get();
            redisUtil.removeZSet("rank",redisEntity.getLogin());

        }

        ResponseModel<?> responseModel = collectionClientFeign.calcuate(login);
        if(!responseModel.isSuccess()){
            throw new RuntimeException(responseModel.getMessageInfo());
        }
        LinkedHashMap data = (LinkedHashMap) responseModel.getData();
        TalentRankEntity talentRankEntity = new TalentRankEntity();
        List<Double> historyScore = new ArrayList();
        Optional<TalentRankEntity> talentRankOpt = talentRankRepository.findByGitIdAndDeletedFalse((String) data.get("gitId"));
        if(talentRankOpt.isPresent()){
            talentRankEntity = talentRankOpt.get();
            if(StringUtils.hasText(talentRankEntity.getScoreHistory())){
                historyScore = JSONObject.parseArray(talentRankEntity.getScoreHistory(), Double.class);

            }
            if(talentRankProjectRepository.findAllByTalentRankIdAndDeletedFalse(talentRankEntity.getId()).isPresent()){
                talentRankProjectRepository.deleteAllByTalentRankId(talentRankEntity.getId());
            }

        }
        //添加历史
        historyScore.add((Double) data.get("rank"));
        talentRankEntity.setScoreHistory( JSONObject.toJSONString(historyScore));
        talentRankEntity.setTalentRank(BigDecimal.valueOf((Double) data.get("rank")));
        talentRankEntity.setName((String) data.get("name"));
        talentRankEntity.setGitId((String) data.get("gitId"));
        talentRankEntity.setbio((String) data.get("bio"));
        talentRankEntity.setLocation((String) data.get("location"));
        talentRankEntity.setAvatarUrl((String) data.get("avatarUrl"));
        talentRankEntity.setAreas((String)data.get("areas"));
        talentRankEntity.setAreaCredence((String)data.get("areaCredence"));
        talentRankEntity.setLocationCredence((String)data.get("locationCredence"));
        talentRankEntity.setLocation((String)data.get("location"));
        talentRankEntity.setLogin((String)data.get("login"));
       // talentRankEntity.setAreas((List<String>) data.get("areas"));
        TalentRankEntity talentRankEntityProjectEntity = talentRankRepository.save(talentRankEntity);
       String talentRankId =  talentRankEntityProjectEntity.getId();
       List<LinkedHashMap> talentRankProjectTrans = (List<LinkedHashMap>) data.get("repositoryTrans");
       List<TalentRankProjectVO> tranRankProjectVoList = new ArrayList<>();
       for(LinkedHashMap hm:talentRankProjectTrans){
            TalentRankProjectEntity talentRankProject  = new TalentRankProjectEntity();
            TalentRankProjectVO talentRankProjectVO = new TalentRankProjectVO();
            talentRankProject.setUrl((String) hm.get("url"));
            talentRankProject.setName((String) hm.get("repositoryName"));
            talentRankProject.setStarCount((Integer) hm.get("starCount"));
            talentRankProject.setTalentRankId(talentRankId);
            talentRankProjectVO.setUrl((String) hm.get("url"));
            talentRankProjectVO.setRepositoryName((String) hm.get("repositoryName"));
            talentRankProjectVO.setStarCount((Integer) hm.get("starCount"));
            tranRankProjectVoList.add(talentRankProjectVO);
            talentRankProjectRepository.save(talentRankProject);
       }

        TalentRankVO talentRankVO = new TalentRankVO();
        BeanUtils.copyProperties(talentRankEntity,talentRankVO);
        Optional<TalentRankByPageRankEntity> talentRankByPageRankEntity=talentRankByPageRankRepository.findByGitId(talentRankVO.getGitId());
        if(talentRankByPageRankEntity.isPresent()){
            talentRankVO.setTalentRankByPageRank(talentRankByPageRankEntity.get().getScoreByPageRank());
        }
        talentRankVO.setTalentRankProjectVOList(tranRankProjectVoList);
       redisUtil.addZSet("rank", talentRankVO, talentRankEntity.getTalentRank().doubleValue());
        return talentRankVO;
    }

    @Override
    public List<TalentRankVO> findAll() {
        List<TalentRankVO> talentRanks = redisUtil.getZSet("rank");
        if(!Collections.isEmpty(talentRanks)){
            return  talentRanks;
        }
        List<TalentRankEntity> talentRankEntityList = talentRankRepository.findAllByOrderByTalentRankDesc().get();
        List<TalentRankVO> talentRankVOList = new ArrayList<>();
        for(TalentRankEntity talentRankEntity : talentRankEntityList){
            List<TalentRankProjectEntity> talentRankProjectEntity = talentRankProjectRepository.findAllByTalentRankIdAndDeletedFalse(talentRankEntity.getId()).get();
            List<TalentRankProjectVO> talentRankProjectVOList = new ArrayList<>();
            for(TalentRankProjectEntity entity: talentRankProjectEntity){
                TalentRankProjectVO talentRankProjectVO = new TalentRankProjectVO();
                talentRankProjectVO.setStarCount(entity.getStarCount());
                talentRankProjectVO.setUrl(entity.getUrl());
                talentRankProjectVO.setRepositoryName(entity.getName());
                talentRankProjectVOList.add(talentRankProjectVO);
            }
            TalentRankVO talentRankVO = new TalentRankVO();
            BeanUtils.copyProperties(talentRankEntity,talentRankVO);
            talentRankVO.setTalentRankProjectVOList(talentRankProjectVOList);
            if(StringUtils.hasText(talentRankEntity.getScoreHistory())){
                talentRankVO.setScoreHistory(JSONObject.parseArray(talentRankEntity.getScoreHistory(), Double.class));
            }
          talentRankVOList.add(talentRankVO);
            redisUtil.addZSet("rank",talentRankVO,talentRankVO.getTalentRank().doubleValue());
        }
        return redisUtil.getZSet("rank");

    }

    @Override
    public void pageRank() {
        collectionClientFeign.pageRank();
    }


}
