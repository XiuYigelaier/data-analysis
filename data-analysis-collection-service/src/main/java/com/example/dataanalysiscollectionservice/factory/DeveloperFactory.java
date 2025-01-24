package com.example.dataanalysiscollectionservice.factory;

import com.example.dataanalysiscollectionservice.pojo.po.mysql.DeveloperAndProjectRelationShipCollectionPO;
import com.example.dataanalysiscollectionservice.pojo.po.mysql.DeveloperCollectionPO;
import com.example.dataanalysiscollectionservice.pojo.po.mysql.DeveloperProjectCollectionPO;
import com.example.dataanalysiscollectionservice.pojo.vo.mysql.DeveloperCollectionVO;
import com.example.dataanalysiscollectionservice.pojo.vo.mysql.DeveloperProjectCollectionVO;
import com.example.dataanalysiscollectionservice.repository.mysql.DeveloperAndProjectRelationShipCollectionRepository;
import com.example.dataanalysiscollectionservice.repository.mysql.DeveloperCollectionRepository;
import com.example.dataanalysiscollectionservice.repository.mysql.DeveloperProjectCollectionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DeveloperFactory {

    @Autowired
    DeveloperCollectionRepository developerCollectionRepository;
    @Autowired
    DeveloperProjectCollectionRepository developerProjectCollectionRepository;
    @Autowired
    DeveloperAndProjectRelationShipCollectionRepository developerAndProjectRelationShipCollectionRepository;


    public List<DeveloperCollectionVO> toDeveloperCollectionVOList(List<DeveloperCollectionPO> developerCollectionPOS) {
        List<String> developerIds = developerCollectionPOS.stream().map(DeveloperCollectionPO::getGitId).collect(Collectors.toList());
        Map<String, List<DeveloperAndProjectRelationShipCollectionPO>> groupedByDeveloperId = new LinkedHashMap<>();
        List<String> developerProjectIds = Stream.of("0").collect(Collectors.toList());

        List<String> projectIds = new ArrayList<>();
        developerAndProjectRelationShipCollectionRepository.findAllByDeletedFalseAndDeveloperIdIn(developerIds).forEach(
                developerAndProjectRelationShipCollectionPO -> {
                    List<DeveloperAndProjectRelationShipCollectionPO> developerAndProjectRelationShipCollectionPOS = groupedByDeveloperId.getOrDefault(developerAndProjectRelationShipCollectionPO.getDeveloperId(), new ArrayList<>());
                    developerAndProjectRelationShipCollectionPOS.add(developerAndProjectRelationShipCollectionPO);
                    groupedByDeveloperId.put(developerAndProjectRelationShipCollectionPO.getDeveloperId(), developerAndProjectRelationShipCollectionPOS);
                    developerProjectIds.add(developerAndProjectRelationShipCollectionPO.getDeveloperId());
                     projectIds.add(developerAndProjectRelationShipCollectionPO.getProjectId());
                }
        );
        Map<String, DeveloperProjectCollectionPO> idAndDeveloperProjectPO = developerProjectCollectionRepository.findAllByDeletedFalseAndGitIdIn(projectIds).stream().collect(Collectors.toMap(DeveloperProjectCollectionPO::getGitId, developerProjectCollectionPO -> developerProjectCollectionPO));
        List<DeveloperCollectionVO> result = new ArrayList<>();
        developerCollectionPOS.forEach(
                developer -> {
                    DeveloperCollectionVO developerCollectionVO = new DeveloperCollectionVO();
                    BeanUtils.copyProperties(developer, developerCollectionVO);
                    List<DeveloperAndProjectRelationShipCollectionPO> developerAndProjectRelationShipCollectionPOS = groupedByDeveloperId.getOrDefault(developer.getGitId(), new ArrayList<>());
                    List<DeveloperProjectCollectionVO> developerProjectCollectionVOS = new ArrayList<>();
                    developerAndProjectRelationShipCollectionPOS.forEach(
                            developerAndProjectRelationShipCollectionPO -> {
                                DeveloperProjectCollectionVO developerProjectCollectionVO = new DeveloperProjectCollectionVO();
                                BeanUtils.copyProperties(developerAndProjectRelationShipCollectionPO, developerProjectCollectionVO);
                                DeveloperProjectCollectionPO developerProjectCollectionPO = idAndDeveloperProjectPO.getOrDefault(developerAndProjectRelationShipCollectionPO.getProjectId(), new DeveloperProjectCollectionPO());
                                //开发者项目
                                developerProjectCollectionVO.setDeveloperId(developerCollectionVO.getGitId());
                                developerProjectCollectionVO.setDescription(developerProjectCollectionPO.getDescription());
                                developerProjectCollectionVO.setName(developerProjectCollectionPO.getName());
                                developerProjectCollectionVO.setGitId(developerProjectCollectionPO.getGitId());
                                developerProjectCollectionVO.setLanguage(developerProjectCollectionPO.getLanguage());
                                developerProjectCollectionVO.setUrl(developerProjectCollectionPO.getUrl());
                                developerProjectCollectionVO.setIssuesCount(developerProjectCollectionPO.getIssuesCount());
                                developerProjectCollectionVO.setCommentsCount(developerProjectCollectionPO.getCommentsCount());
                                developerProjectCollectionVO.setWatchersCount(developerProjectCollectionPO.getWatchersCount());
                                developerProjectCollectionVO.setStargazersCount(developerProjectCollectionPO.getStargazersCount());
                                developerProjectCollectionVOS.add(developerProjectCollectionVO);
                            }
                    );
                    developerCollectionVO.setDeveloperProjectCollectionList(developerProjectCollectionVOS);
                    result.add(developerCollectionVO);
                }
        );
        return result;
    }

    public DeveloperCollectionVO toDeveloperCollectionVO(String login) {
        DeveloperCollectionPO developer = developerCollectionRepository.findByLoginAndDeletedFalse(login).orElseGet(DeveloperCollectionPO::new);
        List<DeveloperAndProjectRelationShipCollectionPO> developerAndProjectRelationShipCollectionPOS = developerAndProjectRelationShipCollectionRepository.findAllByDeletedFalseAndDeveloperId(developer.getGitId());
        List<String> projectIds = Stream.of("0").collect(Collectors.toList());
        developerAndProjectRelationShipCollectionPOS.forEach(
                developerAndProjectRelationShipCollectionPO -> {
                    projectIds.add(developerAndProjectRelationShipCollectionPO.getProjectId());
                }
        );
        Map<String, DeveloperProjectCollectionPO> idAndDeveloperProjectPO = developerProjectCollectionRepository.findAllByDeletedFalseAndGitIdIn(projectIds).stream().collect(Collectors.toMap(DeveloperProjectCollectionPO::getGitId, developerProjectCollectionPO -> developerProjectCollectionPO));
        DeveloperCollectionVO developerCollectionVO = new DeveloperCollectionVO();
        BeanUtils.copyProperties(developer, developerCollectionVO);
        List<DeveloperProjectCollectionVO> developerProjectCollectionVOS = new ArrayList<>();
        developerAndProjectRelationShipCollectionPOS.forEach(
                developerAndProjectRelationShipCollectionPO -> {
                    DeveloperProjectCollectionVO developerProjectCollectionVO = new DeveloperProjectCollectionVO();
                    BeanUtils.copyProperties(developerAndProjectRelationShipCollectionPO, developerProjectCollectionVO);
                    DeveloperProjectCollectionPO developerProjectCollectionPO = idAndDeveloperProjectPO.getOrDefault(developerAndProjectRelationShipCollectionPO.getProjectId(), new DeveloperProjectCollectionPO());
                    //开发者项目
                    developerProjectCollectionVO.setDescription(developerProjectCollectionPO.getDescription());
                    developerProjectCollectionVO.setName(developerProjectCollectionPO.getName());
                    developerProjectCollectionVO.setGitId(developerProjectCollectionPO.getGitId());
                    developerProjectCollectionVO.setLanguage(developerProjectCollectionPO.getLanguage());
                    developerProjectCollectionVO.setUrl(developerProjectCollectionPO.getUrl());
                    developerProjectCollectionVO.setIssuesCount(developerProjectCollectionPO.getIssuesCount());
                    developerProjectCollectionVO.setCommentsCount(developerProjectCollectionPO.getCommentsCount());
                    developerProjectCollectionVO.setWatchersCount(developerProjectCollectionPO.getWatchersCount());
                    developerProjectCollectionVO.setStargazersCount(developerProjectCollectionPO.getStargazersCount());
                    developerProjectCollectionVOS.add(developerProjectCollectionVO);
                }
        );
        developerCollectionVO.setDeveloperProjectCollectionList(developerProjectCollectionVOS);
        return developerCollectionVO;

    }

}
