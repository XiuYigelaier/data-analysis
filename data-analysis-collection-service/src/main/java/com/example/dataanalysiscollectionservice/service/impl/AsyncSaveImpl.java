package com.example.dataanalysiscollectionservice.service.impl;


import com.example.core.entity.Developer;
import com.example.core.entity.DeveloperAndProjectRelationShip;
import com.example.core.entity.Project;
import com.example.core.repository.DeveloperAndProjectRelationShipRepository;
import com.example.core.repository.DeveloperRepository;
import com.example.core.repository.ProjectRepository;
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
        Developer developer = new Developer();
        developer.setLogin(login);
        Map data = (Map) result.get("data");
        Map user = (Map) data.get("user");
        if (ObjectUtils.isEmpty(data.get("user"))) {
            throw new RuntimeException("无此用户信息"+login + result.get("errors"));
        }
        ;
        if (developerRepository.findByGitIdAndDeletedFalse((String) user.get("id")).isPresent()) {
            developer = developerRepository.findByGitIdAndDeletedFalse((String) user.get("id")).get();
        }
        developer.setName((String) user.get("name"));
        developer.setBlo((String) user.get("bio"));
        developer.setAvatarUrl((String) user.get("avatarUrl"));
        developer.setGitId((String) user.get("id"));
        developer.setDeveloperProgramMemberFlag((Boolean) user.get("isDeveloperProgramMember"));
        developer.setBountyHunterFlag((Boolean) user.get("isBountyHunter"));
        developer.setCampusExpertFlag((Boolean) user.get("isCampusExpert"));
        Map followerMap = (Map) user.get("followers");
        developer.setFollowersCount((Integer) followerMap.get("totalCount"));
        Map gistMap = (Map) user.get("gists");
        developer.setPublicGistsCount((Integer) gistMap.get("totalCount"));
        LinkedHashMap repositoriesList = (LinkedHashMap) user.get("repositories");
        developer.setPublicReposCount((Integer) repositoriesList.get("totalCount"));
        String developerId = developerRepository.save(developer).getId();

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
                    Project project = new Project();
                    Optional<Project> projectOpt = projectRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
                    if (projectOpt.isPresent()) {
                        project = projectOpt.get();
                    }
                    project.setGitId((String) rep.get("id"));
                    project.setUrl((String) rep.get("url"));
                    project.setName((String) rep.get("name"));
                    if(!ObjectUtils.isEmpty(rep.get("primaryLanguage"))){
                        project.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("commitComments"))){
                        project.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("watchers"))){
                        project.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
                    }
                   if(!ObjectUtils.isEmpty(rep.get("issues"))){
                       project.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
                   }
                    project.setStargazersCount((Integer) rep.get("stargazerCount"));
                    String projectId = projectRepository.save(project).getId();
                    Optional<DeveloperAndProjectRelationShip> developerAndProjectRelationShipOpt = developerAndProjectRelationShipRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
                    DeveloperAndProjectRelationShip developerAndProjectRelationShip = new DeveloperAndProjectRelationShip();
                    if (developerAndProjectRelationShipOpt.isPresent()) {
                        developerAndProjectRelationShip = developerAndProjectRelationShipOpt.get();
                    }
                    developerAndProjectRelationShip.setDeveloperId(developerId);
                    developerAndProjectRelationShip.setProjectId(projectId);
                    developerAndProjectRelationShip.setPrimaryLanguage(project.getLanguage());
                    developerAndProjectRelationShip.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
                    developerAndProjectRelationShip.setPullRequestReviewEventCount((Integer) ((Map) pullRep.get("contributions")).get("totalCount"));
                    developerAndProjectRelationShipRepository.save(developerAndProjectRelationShip);
                }
        );

        issueContributionsByRepositoryList.forEach(
                issueRep -> {
                    LinkedHashMap rep = (LinkedHashMap) issueRep.get("repository");
                    Project project = new Project();
                    Optional<Project> projectOpt = projectRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
                    if (projectOpt.isPresent()) {
                        project = projectOpt.get();
                    }
                    project.setGitId((String) rep.get("id"));
                    project.setUrl((String) rep.get("url"));
                    project.setName((String) rep.get("name"));
                    if(!ObjectUtils.isEmpty(rep.get("primaryLanguage"))){
                        project.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("commitComments"))){
                        project.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("watchers"))){
                        project.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("issues"))){
                        project.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
                    }
                    project.setStargazersCount((Integer) rep.get("stargazerCount"));
                    String projectId = projectRepository.save(project).getId();
                    Optional<DeveloperAndProjectRelationShip> developerAndProjectRelationShipOpt = developerAndProjectRelationShipRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
                    DeveloperAndProjectRelationShip developerAndProjectRelationShip = new DeveloperAndProjectRelationShip();
                    if (developerAndProjectRelationShipOpt.isPresent()) {
                        developerAndProjectRelationShip = developerAndProjectRelationShipOpt.get();
                    }
                    developerAndProjectRelationShip.setDeveloperId(developerId);
                    developerAndProjectRelationShip.setProjectId(projectId);
                    developerAndProjectRelationShip.setPrimaryLanguage(project.getLanguage());
                    developerAndProjectRelationShip.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
                    developerAndProjectRelationShip.setIssuesCommentEventCount((Integer) ((Map) issueRep.get("contributions")).get("totalCount"));
                    developerAndProjectRelationShipRepository.save(developerAndProjectRelationShip);
                }
        );

        commitContributionsByRepositoryList.forEach(
                commitRep -> {
                    LinkedHashMap rep = (LinkedHashMap) commitRep.get("repository");
                    Project project = new Project();
                    Optional<Project> projectOpt = projectRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
                    if (projectOpt.isPresent()) {
                        project = projectOpt.get();
                    }
                    project.setGitId((String) rep.get("id"));
                    project.setUrl((String) rep.get("url"));
                    project.setName((String) rep.get("name"));
                    if(!ObjectUtils.isEmpty(rep.get("primaryLanguage"))){
                        project.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("commitComments"))){
                        project.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("watchers"))){
                        project.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
                    }
                    if(!ObjectUtils.isEmpty(rep.get("issues"))){
                        project.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
                    }
                    project.setStargazersCount((Integer) rep.get("stargazerCount"));
                    String projectId = projectRepository.save(project).getId();
                    Optional<DeveloperAndProjectRelationShip> developerAndProjectRelationShipOpt = developerAndProjectRelationShipRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
                    DeveloperAndProjectRelationShip developerAndProjectRelationShip = new DeveloperAndProjectRelationShip();
                    if (developerAndProjectRelationShipOpt.isPresent()) {
                        developerAndProjectRelationShip = developerAndProjectRelationShipOpt.get();
                    }
                    developerAndProjectRelationShip.setDeveloperId(developerId);
                    developerAndProjectRelationShip.setProjectId(projectId);
                    developerAndProjectRelationShip.setPrimaryLanguage(project.getLanguage());
                    developerAndProjectRelationShip.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
                    developerAndProjectRelationShip.setCommitCount((Integer) ((Map) commitRep.get("contributions")).get("totalCount"));
                    developerAndProjectRelationShipRepository.save(developerAndProjectRelationShip);
                }
        );

    }



}
