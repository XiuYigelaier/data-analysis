package com.example.dataanalysiscalculateservice.service;

import com.example.core.enums.ProjectClassificationEnum;
import com.example.dataanalysiscalculateservice.mapper.DeveloperGraphMapper;
import com.example.dataanalysiscalculateservice.pojo.po.mysql.TalentRankProjectPO;
import com.example.dataanalysiscalculateservice.pojo.po.neo4j.DeveloperGraphPO;
import com.example.dataanalysiscalculateservice.pojo.vo.DeveloperGraphVO;
import com.example.dataanalysiscalculateservice.pojo.vo.ProjectClassificationVO;
import com.example.dataanalysiscalculateservice.repository.mysql.TalentRankProjectRepository;
import com.example.dataanalysiscalculateservice.repository.neo4j.DeveloperGraphRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    @Autowired
    TalentRankProjectRepository talentRankProjectRepository;
    @Autowired
    DeveloperGraphRepository developerGraphRepository;
    @Autowired
    DeveloperGraphMapper developerGraphMapper;

    public Map<ProjectClassificationEnum,Long> projectClassificationCount(){
        List<Object[]> results = talentRankProjectRepository.findProjectCountGroupedByClassification();
        Map<ProjectClassificationEnum, Long> classificationCountMap = new HashMap<>();

        for (Object[] result : results) {
            ProjectClassificationEnum classification = (ProjectClassificationEnum) result[0];
            Long count = (Long) result[1];
            classificationCountMap.put(classification, count);
        }

        return classificationCountMap;
    }

    public  List<ProjectClassificationVO> projectClassificationList(){
       List<TalentRankProjectPO> talentRankProjectPOS =  talentRankProjectRepository.findTop20ByClassificationAndStarCount();
        Map<ProjectClassificationEnum,List<TalentRankProjectPO>> classificationEnumTalentRankProjectPOMap = talentRankProjectPOS.stream().collect(Collectors.groupingBy(TalentRankProjectPO::getClassification));
        List<ProjectClassificationVO> results = new ArrayList<>();

        classificationEnumTalentRankProjectPOMap.forEach(
                (k,v)->{
                    ProjectClassificationVO projectClassificationVO = new ProjectClassificationVO();
                    projectClassificationVO.setProjectClassification(k);
                    v.forEach(
                            talentRankProjectPO -> {
                               ProjectClassificationVO.ProjectClassification_Project talentRankProjectVO =  projectClassificationVO.new ProjectClassification_Project();
                                BeanUtils.copyProperties(talentRankProjectPO, talentRankProjectVO);
                               projectClassificationVO.setProjects(Collections.singletonList(talentRankProjectVO));

                            }
                    );
                    results.add(projectClassificationVO);

                }
        );
        return results;

    }

    public DeveloperGraphVO findDeveloperGraph(String gitId){
        DeveloperGraphPO developerGraphPO = developerGraphRepository.findByDeveloperId(gitId);
        return developerGraphMapper.toVO(developerGraphPO);
    }


}
