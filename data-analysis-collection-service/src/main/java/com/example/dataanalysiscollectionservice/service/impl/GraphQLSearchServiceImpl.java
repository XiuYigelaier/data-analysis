package com.example.dataanalysiscollectionservice.service.impl;

import com.example.core.repository.DeveloperAndProjectRelationShipRepository;
import com.example.core.repository.DeveloperRepository;
import com.example.core.repository.ProjectRepository;
import com.example.core.service.AsyncSave;
import com.example.core.service.GraphQLSearchService;



import org.mountcloud.graphql.GraphqlClient;
import org.mountcloud.graphql.request.query.DefaultGraphqlQuery;
import org.mountcloud.graphql.request.query.GraphqlQuery;
import org.mountcloud.graphql.request.result.ResultAttributtes;
import org.mountcloud.graphql.response.GraphqlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class GraphQLSearchServiceImpl implements GraphQLSearchService {

    @Value("${git.token}")
    String GIT_TOKEN;
    @Value("${git.url}")
    String GIT_URL;
    String FILE_PATH = "D:\\javaProject1\\DataAnalysis\\data-analysis-collection-service\\src\\main\\resources\\query.graphql";

    @Autowired
    AsyncSave asyncSave;
    @Autowired
    DeveloperRepository developerRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    DeveloperAndProjectRelationShipRepository developerAndProjectRelationShipRepository;

    @Transactional
    @Override
    public Map graphqlSearch(String developerName) {
        GraphqlClient graphqlClient = GraphqlClient.buildGraphqlClient(GIT_URL);
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("Authorization", GIT_TOKEN);
        graphqlClient.setHttpHeaders(httpHeaders);
        GraphqlQuery query = new DefaultGraphqlQuery("user");
        query.addParameter("login", developerName);
        query.addResultAttributes("id", "avatarUrl", "location", "bio", "isBountyHunter", "isCampusExpert", "isDeveloperProgramMember","company","pronouns");
        ResultAttributtes contributionsCollection = new ResultAttributtes("contributionsCollection");
        contributionsCollection.addResultAttributes("hasAnyRestrictedContributions", "totalCommitContributions", "totalIssueContributions", "totalPullRequestContributions", "totalRepositoriesWithContributedCommits");
        ResultAttributtes pullRequestReviewContributionsByRepository = new ResultAttributtes("pullRequestReviewContributionsByRepository");
        ResultAttributtes primaryLanguage = new ResultAttributtes("primaryLanguage");
        ResultAttributtes repository = new ResultAttributtes("repository");
        repository.addResultAttributes("name", "url", "stargazerCount", "id");
        ResultAttributtes watchers = new ResultAttributtes("watchers");
        watchers.addResultAttributes("totalCount");
        repository.addResultAttributes(watchers);

        ResultAttributtes issues = new ResultAttributtes("issues");
        issues.addResultAttributes("totalCount");
        repository.addResultAttributes(issues);

        ResultAttributtes commitComments = new ResultAttributtes("commitComments");
        commitComments.addResultAttributes("totalCount");
        repository.addResultAttributes(commitComments);

        primaryLanguage.addResultAttributes("name");

        ResultAttributtes contributions = new ResultAttributtes("contributions");
        contributions.addResultAttributes("totalCount");
        query.addResultAttributes(contributionsCollection);
        pullRequestReviewContributionsByRepository.addResultAttributes(repository);
        contributionsCollection.addResultAttributes(pullRequestReviewContributionsByRepository);
        pullRequestReviewContributionsByRepository.addResultAttributes(contributions);
        repository.addResultAttributes(primaryLanguage);

        ResultAttributtes issueContributionsByRepository = new ResultAttributtes("issueContributionsByRepository");
        issueContributionsByRepository.addResultAttributes(repository);
        contributionsCollection.addResultAttributes(issueContributionsByRepository);
        issueContributionsByRepository.addResultAttributes(contributions);

        ResultAttributtes commitContributionsByRepository = new ResultAttributtes("commitContributionsByRepository");
        commitContributionsByRepository.addResultAttributes(repository);
        commitContributionsByRepository.addResultAttributes(contributions);

        contributionsCollection.addResultAttributes(commitContributionsByRepository);
        ResultAttributtes repositories = new ResultAttributtes("repositories");
        repositories.addResultAttributes("totalCount");
        query.addResultAttributes(repositories);

        ResultAttributtes followers = new ResultAttributtes("followers");
        followers.addResultAttributes("totalCount");
        query.addResultAttributes(followers);

        ResultAttributtes gists = new ResultAttributtes("gists");
        gists.addResultAttributes("totalCount");
        query.addResultAttributes(gists);


        Map result = null;
        try {
            GraphqlResponse response = graphqlClient.doQuery(query);
            result = response.getData();
        } catch (IOException e) {
            throw new RuntimeException("git请求失败:" + result.get("errors"));
        }



//        Developer developer = new Developer();
//        developer.setName(developerName);
//
//        Map data = (Map) result.get("data");
//        Map user = (Map) data.get("user");
//        if (ObjectUtils.isEmpty(data.get("user"))) {
//            throw new RuntimeException("无此用户信息" + result.get("errors"));
//        }
//        ;
//        if (developerRepository.findByGitIdAndDeletedFalse((String) user.get("id")).isPresent()) {
//            developer = developerRepository.findByGitIdAndDeletedFalse((String) user.get("id")).get();
//        }
//        developer.setBlo((String) user.get("bio"));
//        developer.setAvatarUrl((String) user.get("avatarUrl"));
//        developer.setGitId((String) user.get("id"));
//        developer.setDeveloperProgramMemberFlag((Boolean) user.get("isDeveloperProgramMember"));
//        developer.setBountyHunterFlag((Boolean) user.get("isBountyHunter"));
//        developer.setCampusExpertFlag((Boolean) user.get("isCampusExpert"));
//        Map followerMap = (Map) user.get("followers");
//        developer.setFollowersCount((Integer) followerMap.get("totalCount"));
//        Map gistMap = (Map) user.get("gists");
//        developer.setPublicGistsCount((Integer) gistMap.get("totalCount"));
//        LinkedHashMap repositoriesList = (LinkedHashMap) user.get("repositories");
//        developer.setPublicReposCount((Integer) repositoriesList.get("totalCount"));
//        String developerId = developerRepository.save(developer).getId();
//
//        //删除关联表
//        if (developerAndProjectRelationShipRepository.findAllByDeveloperIdAndDeletedFalse(developerId).isPresent()) {
//            developerAndProjectRelationShipRepository.deleteAllByDeveloperIdAndDeletedFalse(developerId);
//        }
//        LinkedHashMap contributionsCollectionMap = (LinkedHashMap) user.get("contributionsCollection");
//        ArrayList<LinkedHashMap> pullRequestReviewContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("pullRequestReviewContributionsByRepository");
//        ArrayList<LinkedHashMap> issueContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("issueContributionsByRepository");
//        ArrayList<LinkedHashMap> commitContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("commitContributionsByRepository");
//
//        pullRequestReviewContributionsByRepositoryList.forEach(
//                pullRep -> {
//                    LinkedHashMap rep = (LinkedHashMap) pullRep.get("repository");
//                    Project project = new Project();
//                    Optional<Project> projectOpt = projectRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
//                    if (projectOpt.isPresent()) {
//                        project = projectOpt.get();
//                    }
//                    project.setGitId((String) rep.get("id"));
//                    project.setUrl((String) rep.get("url"));
//                    project.setName((String) rep.get("name"));
//                    project.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
//                    project.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
//                    project.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
//                    project.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
//                    project.setStargazersCount((Integer) rep.get("stargazerCount"));
//                    String projectId = projectRepository.save(project).getId();
//                    Optional<DeveloperAndProjectRelationShip> developerAndProjectRelationShipOpt = developerAndProjectRelationShipRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
//                    DeveloperAndProjectRelationShip developerAndProjectRelationShip = new DeveloperAndProjectRelationShip();
//                    if (developerAndProjectRelationShipOpt.isPresent()) {
//                        developerAndProjectRelationShip = developerAndProjectRelationShipOpt.get();
//                    }
//                    developerAndProjectRelationShip.setDeveloperId(developerId);
//                    developerAndProjectRelationShip.setProjectId(projectId);
//                    developerAndProjectRelationShip.setPrimaryLanguage(project.getLanguage());
//                    developerAndProjectRelationShip.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
//                    developerAndProjectRelationShip.setPullRequestReviewEventCount((Integer) ((Map) pullRep.get("contributions")).get("totalCount"));
//                    developerAndProjectRelationShipRepository.save(developerAndProjectRelationShip);
//                }
//        );
//
//        issueContributionsByRepositoryList.forEach(
//                issueRep -> {
//                    LinkedHashMap rep = (LinkedHashMap) issueRep.get("repository");
//                    Project project = new Project();
//                    Optional<Project> projectOpt = projectRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
//                    if (projectOpt.isPresent()) {
//                        project = projectOpt.get();
//                    }
//                    project.setGitId((String) rep.get("id"));
//                    project.setUrl((String) rep.get("url"));
//                    project.setName((String) rep.get("name"));
//                    project.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
//                    project.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
//                    project.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
//                    project.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
//                    project.setStargazersCount((Integer) rep.get("stargazerCount"));
//                    String projectId = projectRepository.save(project).getId();
//                    Optional<DeveloperAndProjectRelationShip> developerAndProjectRelationShipOpt = developerAndProjectRelationShipRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
//                    DeveloperAndProjectRelationShip developerAndProjectRelationShip = new DeveloperAndProjectRelationShip();
//                    if (developerAndProjectRelationShipOpt.isPresent()) {
//                        developerAndProjectRelationShip = developerAndProjectRelationShipOpt.get();
//                    }
//                    developerAndProjectRelationShip.setDeveloperId(developerId);
//                    developerAndProjectRelationShip.setProjectId(projectId);
//                    developerAndProjectRelationShip.setPrimaryLanguage(project.getLanguage());
//                    developerAndProjectRelationShip.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
//                    developerAndProjectRelationShip.setIssuesCommentEventCount((Integer) ((Map) issueRep.get("contributions")).get("totalCount"));
//                    developerAndProjectRelationShipRepository.save(developerAndProjectRelationShip);
//                }
//        );
//
//        commitContributionsByRepositoryList.forEach(
//                commitRep -> {
//                    LinkedHashMap rep = (LinkedHashMap) commitRep.get("repository");
//                    Project project = new Project();
//                    Optional<Project> projectOpt = projectRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
//                    if (projectOpt.isPresent()) {
//                        project = projectOpt.get();
//                    }
//                    project.setGitId((String) rep.get("id"));
//                    project.setUrl((String) rep.get("url"));
//                    project.setName((String) rep.get("name"));
//                    project.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
//                    project.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
//                    project.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
//                    project.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
//                    project.setStargazersCount((Integer) rep.get("stargazerCount"));
//                    String projectId = projectRepository.save(project).getId();
//                    Optional<DeveloperAndProjectRelationShip> developerAndProjectRelationShipOpt = developerAndProjectRelationShipRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
//                    DeveloperAndProjectRelationShip developerAndProjectRelationShip = new DeveloperAndProjectRelationShip();
//                    if (developerAndProjectRelationShipOpt.isPresent()) {
//                        developerAndProjectRelationShip = developerAndProjectRelationShipOpt.get();
//                    }
//                    developerAndProjectRelationShip.setDeveloperId(developerId);
//                    developerAndProjectRelationShip.setProjectId(projectId);
//                    developerAndProjectRelationShip.setPrimaryLanguage(project.getLanguage());
//                    developerAndProjectRelationShip.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
//                    developerAndProjectRelationShip.setCommitCount((Integer) ((Map) commitRep.get("contributions")).get("totalCount"));
//                    developerAndProjectRelationShipRepository.save(developerAndProjectRelationShip);
//                }
//        );
        asyncSave.save(developerName,result);
        return  result;

    }




}
