package com.example.dataanalysiscollectionservice.service.impl;


import com.example.core.pojo.entity.mysql.DeveloperProjectEntity;
import com.example.core.pojo.entity.mysql.DeveloperEntity;
import com.example.core.pojo.entity.mysql.DeveloperAndProjectRelationShipEntity;
import com.example.core.repository.mysql.DeveloperAndProjectRelationShipRepository;
import com.example.core.repository.mysql.DeveloperRepository;
import com.example.core.repository.mysql.ProjectRepository;
import com.example.core.service.AsyncSave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AsyncSaveImpl implements AsyncSave {
    @Autowired
    DeveloperRepository developerRepository;
    @Autowired
    DeveloperAndProjectRelationShipRepository developerAndProjectRelationShipRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Async
    public  void save (String login, Map result) {
        DeveloperEntity developerEntity = new DeveloperEntity();
        developerEntity.setLogin(login);
        Map data = (Map) result.get("data");
        Map user = (Map) data.get("user");
        if (ObjectUtils.isEmpty(data.get("user"))) {
            throw new RuntimeException("无此用户信息"+login + result.get("errors"));
        }
        ;
        if (developerRepository.findByGitIdAndDeletedFalse((String) user.get("id")).isPresent()) {
            developerEntity = developerRepository.findByGitIdAndDeletedFalse((String) user.get("id")).get();
        }
        developerEntity.setName((String) user.get("name"));
        developerEntity.setBlo((String) user.get("bio"));
        developerEntity.setAvatarUrl((String) user.get("avatarUrl"));
        developerEntity.setGitId((String) user.get("id"));
        developerEntity.setDeveloperProgramMemberFlag((Boolean) user.get("isDeveloperProgramMember"));
        developerEntity.setBountyHunterFlag((Boolean) user.get("isBountyHunter"));
        developerEntity.setCampusExpertFlag((Boolean) user.get("isCampusExpert"));
        Map followerMap = (Map) user.get("followers");
        developerEntity.setFollowersCount((Integer) followerMap.get("totalCount"));
        Map gistMap = (Map) user.get("gists");
        developerEntity.setPublicGistsCount((Integer) gistMap.get("totalCount"));
        LinkedHashMap repositoriesList = (LinkedHashMap) user.get("repositories");
        developerEntity.setPublicReposCount((Integer) repositoriesList.get("totalCount"));
        String developerId = developerRepository.save(developerEntity).getId();

        //删除关联表
        if (developerAndProjectRelationShipRepository.findAllByDeveloperIdAndDeletedFalse(developerId).isPresent()) {
            developerAndProjectRelationShipRepository.deleteAllByDeveloperIdAndDeletedFalse(developerId);
        }
        LinkedHashMap contributionsCollectionMap = (LinkedHashMap) user.get("contributionsCollection");
        ArrayList<LinkedHashMap> pullRequestReviewContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("pullRequestReviewContributionsByRepository");
        ArrayList<LinkedHashMap> issueContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("issueContributionsByRepository");
        ArrayList<LinkedHashMap> commitContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("commitContributionsByRepository");

        pullRequestReviewContributionsByRepositoryList.forEach(
                pullRep -> {
                    LinkedHashMap rep = (LinkedHashMap) pullRep.get("repository");
                    DeveloperProjectEntity developerProjectEntity = new DeveloperProjectEntity();
                    Optional<DeveloperProjectEntity> projectOpt = projectRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
                    if (projectOpt.isPresent()) {
                        developerProjectEntity = projectOpt.get();
                    }
                    developerProjectEntity.setGitId((String) rep.get("id"));
                    developerProjectEntity.setUrl((String) rep.get("url"));
                    developerProjectEntity.setName((String) rep.get("name"));
                    if(!ObjectUtils.isEmpty(rep.get("primaryLanguage"))){
                        developerProjectEntity.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("commitComments"))){
                        developerProjectEntity.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("watchers"))){
                        developerProjectEntity.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
                    }
                   if(!ObjectUtils.isEmpty(rep.get("issues"))){
                       developerProjectEntity.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
                   }
                    developerProjectEntity.setStargazersCount((Integer) rep.get("stargazerCount"));
                    String projectId = projectRepository.save(developerProjectEntity).getId();
                    Optional<DeveloperAndProjectRelationShipEntity> developerAndProjectRelationShipOpt = developerAndProjectRelationShipRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
                    DeveloperAndProjectRelationShipEntity developerAndProjectRelationShipEntity = new DeveloperAndProjectRelationShipEntity();
                    if (developerAndProjectRelationShipOpt.isPresent()) {
                        developerAndProjectRelationShipEntity = developerAndProjectRelationShipOpt.get();
                    }
                    developerAndProjectRelationShipEntity.setDeveloperId(developerId);
                    developerAndProjectRelationShipEntity.setProjectId(projectId);
                    developerAndProjectRelationShipEntity.setPrimaryLanguage(developerProjectEntity.getLanguage());
                    developerAndProjectRelationShipEntity.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
                    developerAndProjectRelationShipEntity.setPullRequestReviewEventCount((Integer) ((Map) pullRep.get("contributions")).get("totalCount"));
                    developerAndProjectRelationShipRepository.save(developerAndProjectRelationShipEntity);
                }
        );

        issueContributionsByRepositoryList.forEach(
                issueRep -> {
                    LinkedHashMap rep = (LinkedHashMap) issueRep.get("repository");
                    DeveloperProjectEntity developerProjectEntity = new DeveloperProjectEntity();
                    Optional<DeveloperProjectEntity> projectOpt = projectRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
                    if (projectOpt.isPresent()) {
                        developerProjectEntity = projectOpt.get();
                    }
                    developerProjectEntity.setGitId((String) rep.get("id"));
                    developerProjectEntity.setUrl((String) rep.get("url"));
                    developerProjectEntity.setName((String) rep.get("name"));
                    if(!ObjectUtils.isEmpty(rep.get("primaryLanguage"))){
                        developerProjectEntity.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("commitComments"))){
                        developerProjectEntity.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("watchers"))){
                        developerProjectEntity.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("issues"))){
                        developerProjectEntity.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
                    }
                    developerProjectEntity.setStargazersCount((Integer) rep.get("stargazerCount"));
                    String projectId = projectRepository.save(developerProjectEntity).getId();
                    Optional<DeveloperAndProjectRelationShipEntity> developerAndProjectRelationShipOpt = developerAndProjectRelationShipRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
                    DeveloperAndProjectRelationShipEntity developerAndProjectRelationShipEntity = new DeveloperAndProjectRelationShipEntity();
                    if (developerAndProjectRelationShipOpt.isPresent()) {
                        developerAndProjectRelationShipEntity = developerAndProjectRelationShipOpt.get();
                    }
                    developerAndProjectRelationShipEntity.setDeveloperId(developerId);
                    developerAndProjectRelationShipEntity.setProjectId(projectId);
                    developerAndProjectRelationShipEntity.setPrimaryLanguage(developerProjectEntity.getLanguage());
                    developerAndProjectRelationShipEntity.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
                    developerAndProjectRelationShipEntity.setIssuesCommentEventCount((Integer) ((Map) issueRep.get("contributions")).get("totalCount"));
                    developerAndProjectRelationShipRepository.save(developerAndProjectRelationShipEntity);
                }
        );

        commitContributionsByRepositoryList.forEach(
                commitRep -> {
                    LinkedHashMap rep = (LinkedHashMap) commitRep.get("repository");
                    DeveloperProjectEntity developerProjectEntity = new DeveloperProjectEntity();
                    Optional<DeveloperProjectEntity> projectOpt = projectRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
                    if (projectOpt.isPresent()) {
                        developerProjectEntity = projectOpt.get();
                    }
                    developerProjectEntity.setDescription((String) rep.get("description"));
                    developerProjectEntity.setGitId((String) rep.get("id"));
                    developerProjectEntity.setUrl((String) rep.get("url"));
                    developerProjectEntity.setName((String) rep.get("name"));
                    if(!ObjectUtils.isEmpty(rep.get("primaryLanguage"))){
                        developerProjectEntity.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("commitComments"))){
                        developerProjectEntity.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("watchers"))){
                        developerProjectEntity.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("issues"))){
                        developerProjectEntity.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
                    }
                    developerProjectEntity.setStargazersCount((Integer) rep.get("stargazerCount"));
                    String projectId = projectRepository.save(developerProjectEntity).getId();
                    Optional<DeveloperAndProjectRelationShipEntity> developerAndProjectRelationShipOpt = developerAndProjectRelationShipRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
                    DeveloperAndProjectRelationShipEntity developerAndProjectRelationShipEntity = new DeveloperAndProjectRelationShipEntity();
                    if (developerAndProjectRelationShipOpt.isPresent()) {
                        developerAndProjectRelationShipEntity = developerAndProjectRelationShipOpt.get();
                    }
                    developerAndProjectRelationShipEntity.setDeveloperId(developerId);
                    developerAndProjectRelationShipEntity.setProjectId(projectId);
                    developerAndProjectRelationShipEntity.setPrimaryLanguage(developerProjectEntity.getLanguage());
                    developerAndProjectRelationShipEntity.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
                    developerAndProjectRelationShipEntity.setCommitCount((Integer) ((Map) commitRep.get("contributions")).get("totalCount"));
                    developerAndProjectRelationShipRepository.save(developerAndProjectRelationShipEntity);
                }
        );

    }



}
