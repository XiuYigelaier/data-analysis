package com.example.dataanalysiscollectionservice.service.impl;

import com.example.dataanalysiscollectionservice.factory.DeveloperFactory;
import com.example.dataanalysiscollectionservice.pojo.po.DeveloperAndProjectRelationShipCollectionPO;
import com.example.dataanalysiscollectionservice.pojo.po.DeveloperCollectionPO;
import com.example.dataanalysiscollectionservice.pojo.po.DeveloperProjectCollectionPO;
import com.example.dataanalysiscollectionservice.pojo.vo.DeveloperCollectionVO;
import com.example.dataanalysiscollectionservice.repository.DeveloperAndProjectRelationShipCollectionRepository;
import com.example.dataanalysiscollectionservice.repository.DeveloperCollectionRepository;
import com.example.dataanalysiscollectionservice.repository.DeveloperProjectCollectionRepository;
import com.example.dataanalysiscollectionservice.service.GraphQLSearchService;


import okhttp3.OkHttpClient;
import org.mountcloud.graphql.GraphqlClient;
import org.mountcloud.graphql.request.query.DefaultGraphqlQuery;
import org.mountcloud.graphql.request.query.GraphqlQuery;

import org.mountcloud.graphql.request.result.ResultAttributtes;
import org.mountcloud.graphql.response.GraphqlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.*;

@Service
public class GraphQLSearchServiceImpl implements GraphQLSearchService {

    @Value("${git.token}")
    String GIT_TOKEN;
    @Value("${git.url}")
    String GIT_URL;

    private static final OkHttpClient client = new OkHttpClient();

    @Autowired
    DeveloperCollectionRepository developerCollectionRepository;
    @Autowired
    DeveloperProjectCollectionRepository developerProjectCollectionRepository;
    @Autowired
    DeveloperAndProjectRelationShipCollectionRepository developerAndProjectRelationShipCollectionRepository;
    @Autowired
    DeveloperFactory developerFactory;

    @Transactional
    @Override
    public void graphqlSearch(String login) {
        GraphqlClient graphqlClient = GraphqlClient.buildGraphqlClient(GIT_URL);
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("Authorization", GIT_TOKEN);
        graphqlClient.setHttpHeaders(httpHeaders);
        GraphqlQuery query = new DefaultGraphqlQuery("user");
        query.addParameter("login", login);
        query.addResultAttributes("id", "name", "avatarUrl", "location", "bio", "isBountyHunter", "isCampusExpert", "isDeveloperProgramMember", "company", "pronouns");
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
        repository.addResultAttributes("description");

        ResultAttributtes issueContributionsByRepository = new ResultAttributtes("issueContributionsByRepository");
        issueContributionsByRepository.addResultAttributes(repository);
        contributionsCollection.addResultAttributes(issueContributionsByRepository);
        issueContributionsByRepository.addResultAttributes(contributions);

        ResultAttributtes commitContributionsByRepository = new ResultAttributtes("commitContributionsByRepository");
        commitContributionsByRepository.addResultAttributes(repository);
        commitContributionsByRepository.addResultAttributes(contributions);

        contributionsCollection.addResultAttributes(commitContributionsByRepository);
        ResultAttributtes repositories = new ResultAttributtes("repositories(first: 20, orderBy: { field: STARGAZERS, direction: DESC })");
        ResultAttributtes nodes = new ResultAttributtes("nodes");
        nodes.addResultAttributes("name", "url", "stargazerCount");
        repositories.addResultAttributes(nodes);
        repositories.addResultAttributes("totalCount");
        query.addResultAttributes(repositories);

        ResultAttributtes followers = new ResultAttributtes("followers");
        followers.addResultAttributes("totalCount");
        query.addResultAttributes(followers);

        ResultAttributtes gists = new ResultAttributtes("gists");
        gists.addResultAttributes("totalCount");
        query.addResultAttributes(gists);


        ResultAttributtes description = new ResultAttributtes("description");
        nodes.addResultAttributes(description);


        ResultAttributtes following = new ResultAttributtes("following(first:100)");
        following.addResultAttributes("totalCount");
        ResultAttributtes followingNode = new ResultAttributtes("nodes");
        followingNode.addResultAttributes("login", "id", "avatarUrl");
        following.addResultAttributes(followingNode);
        query.addResultAttributes(following);


        Map result = null;
        try {
            GraphqlResponse response = graphqlClient.doQuery(query);
            result = response.getData();

        } catch (IOException e) {
            throw new RuntimeException("git请求失败:" + result.get("errors"));
        }
        if (ObjectUtils.isEmpty(result)) {
            throw new RuntimeException("无result：网络错误");
        }

        Map data = (Map) result.get("data");
        if (!ObjectUtils.isEmpty(data.get("user"))) {
            save(login, result);
        }

    }

    public void save(String login, Map result) {
        DeveloperCollectionPO developerEntity = new DeveloperCollectionPO();
        developerEntity.setLogin(login);
        Map data = (Map) result.get("data");
        Map user = (Map) data.get("user");
        if (ObjectUtils.isEmpty(data.get("user"))) {
            throw new RuntimeException("无此用户信息" + login + result.get("errors"));
        }
        ;
        if (developerCollectionRepository.findByGitIdAndDeletedFalse((String) user.get("id")).isPresent()) {
            developerEntity = developerCollectionRepository.findByGitIdAndDeletedFalse((String) user.get("id")).get();
        }
        developerEntity.setName((String) user.get("name"));
        developerEntity.setBio((String) user.get("bio"));
        developerEntity.setAvatarUrl((String) user.get("avatarUrl"));
        developerEntity.setGitId((String) user.get("id"));
        developerEntity.setLocation((String)user.get("location"));
        developerEntity.setCompany((String)user.get("company"));
        developerEntity.setPronouns((String)user.get("pronouns"));
        developerEntity.setDeveloperProgramMemberFlag((Boolean) user.get("isDeveloperProgramMember"));
        developerEntity.setBountyHunterFlag((Boolean) user.get("isBountyHunter"));
        developerEntity.setCampusExpertFlag((Boolean) user.get("isCampusExpert"));
        Map followerMap = (Map) user.get("followers");
        developerEntity.setFollowersCount((Integer) followerMap.get("totalCount"));
        Map gistMap = (Map) user.get("gists");
        developerEntity.setPublicGistsCount((Integer) gistMap.get("totalCount"));
        LinkedHashMap repositoriesList = (LinkedHashMap) user.get("repositories");
        developerEntity.setPublicReposCount((Integer) repositoriesList.get("totalCount"));

        LinkedHashMap contributionsCollectionMap = (LinkedHashMap) user.get("contributionsCollection");
        ArrayList<LinkedHashMap> pullRequestReviewContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("pullRequestReviewContributionsByRepository");
        ArrayList<LinkedHashMap> issueContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("issueContributionsByRepository");
        ArrayList<LinkedHashMap> commitContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("commitContributionsByRepository");
        developerEntity.setTotalCommitContributions((Integer) contributionsCollectionMap.get("totalCommitContributions"));
        developerEntity.setTotalRepositoriesWithCommits((Integer) contributionsCollectionMap.get("totalRepositoriesWithCommits"));
        developerEntity.setTotalIssueContributions((Integer) contributionsCollectionMap.get("totalIssueContributions"));
        developerEntity.setTotalPullRequestContributions((Integer) contributionsCollectionMap.get("totalPullRequestContributions"));
        Map followingMap = (Map) user.get("following");
        String developerId = developerCollectionRepository.save(developerEntity).getId();

        //删除关联表
        developerAndProjectRelationShipCollectionRepository.deleteAllByDeveloperIdAndDeletedFalse(developerId);


        pullRequestReviewContributionsByRepositoryList.forEach(
                pullRep -> {
                    LinkedHashMap rep = (LinkedHashMap) pullRep.get("repository");
                    DeveloperProjectCollectionPO developerProjectCollectionPO = new DeveloperProjectCollectionPO();
                    Optional<DeveloperProjectCollectionPO> projectOpt = developerProjectCollectionRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
                    if (projectOpt.isPresent()) {
                        developerProjectCollectionPO = projectOpt.get();
                    }
                    developerProjectCollectionPO.setGitId((String) rep.get("id"));
                    developerProjectCollectionPO.setUrl((String) rep.get("url"));
                    developerProjectCollectionPO.setName((String) rep.get("name"));
                    if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
                        developerProjectCollectionPO.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
                    }
                    if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
                        developerProjectCollectionPO.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
                    }
                    if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
                        developerProjectCollectionPO.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
                    }
                    if (!ObjectUtils.isEmpty(rep.get("issues"))) {
                        developerProjectCollectionPO.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
                    }
                    developerProjectCollectionPO.setStargazersCount((Integer) rep.get("stargazerCount"));
                    developerProjectCollectionPO.setDescription((String) rep.get("description"));
                    String projectId = developerProjectCollectionRepository.save(developerProjectCollectionPO).getId();
                    Optional<DeveloperAndProjectRelationShipCollectionPO> developerAndProjectRelationShipOpt = developerAndProjectRelationShipCollectionRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
                    DeveloperAndProjectRelationShipCollectionPO developerAndProjectRelationShipCollectionPO = new DeveloperAndProjectRelationShipCollectionPO();
                    if (developerAndProjectRelationShipOpt.isPresent()) {
                        developerAndProjectRelationShipCollectionPO = developerAndProjectRelationShipOpt.get();
                    }
                    developerAndProjectRelationShipCollectionPO.setDeveloperId(developerId);
                    developerAndProjectRelationShipCollectionPO.setProjectId(projectId);
                    developerAndProjectRelationShipCollectionPO.setPrimaryLanguage(developerProjectCollectionPO.getLanguage());
                    developerAndProjectRelationShipCollectionPO.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
                    developerAndProjectRelationShipCollectionPO.setPullRequestReviewEventCount((Integer) ((Map) pullRep.get("contributions")).get("totalCount"));
                    developerAndProjectRelationShipCollectionRepository.save(developerAndProjectRelationShipCollectionPO);
                }
        );

        issueContributionsByRepositoryList.forEach(
                issueRep -> {
                    LinkedHashMap rep = (LinkedHashMap) issueRep.get("repository");
                    DeveloperProjectCollectionPO developerProjectCollectionPO = new DeveloperProjectCollectionPO();
                    Optional<DeveloperProjectCollectionPO> projectOpt = developerProjectCollectionRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
                    if (projectOpt.isPresent()) {
                        developerProjectCollectionPO = projectOpt.get();
                    }
                    developerProjectCollectionPO.setGitId((String) rep.get("id"));
                    developerProjectCollectionPO.setUrl((String) rep.get("url"));
                    developerProjectCollectionPO.setName((String) rep.get("name"));
                    if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
                        developerProjectCollectionPO.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
                    }
                    if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
                        developerProjectCollectionPO.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
                    }
                    if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
                        developerProjectCollectionPO.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
                    }
                    if (!ObjectUtils.isEmpty(rep.get("issues"))) {
                        developerProjectCollectionPO.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
                    }
                    developerProjectCollectionPO.setStargazersCount((Integer) rep.get("stargazerCount"));
                    developerProjectCollectionPO.setDescription((String) rep.get("description"));
                    String projectId = developerProjectCollectionRepository.save(developerProjectCollectionPO).getId();
                    Optional<DeveloperAndProjectRelationShipCollectionPO> developerAndProjectRelationShipOpt = developerAndProjectRelationShipCollectionRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
                    DeveloperAndProjectRelationShipCollectionPO developerAndProjectRelationShipCollectionPO = new DeveloperAndProjectRelationShipCollectionPO();
                    if (developerAndProjectRelationShipOpt.isPresent()) {
                        developerAndProjectRelationShipCollectionPO = developerAndProjectRelationShipOpt.get();
                    }
                    developerAndProjectRelationShipCollectionPO.setDeveloperId(developerId);
                    developerAndProjectRelationShipCollectionPO.setProjectId(projectId);
                    developerAndProjectRelationShipCollectionPO.setPrimaryLanguage(developerProjectCollectionPO.getLanguage());
                    developerAndProjectRelationShipCollectionPO.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
                    developerAndProjectRelationShipCollectionPO.setIssuesCommentEventCount((Integer) ((Map) issueRep.get("contributions")).get("totalCount"));
                    developerAndProjectRelationShipCollectionRepository.save(developerAndProjectRelationShipCollectionPO);
                }
        );

        commitContributionsByRepositoryList.forEach(
                commitRep -> {
                    LinkedHashMap rep = (LinkedHashMap) commitRep.get("repository");
                    DeveloperProjectCollectionPO developerProjectCollectionPO = new DeveloperProjectCollectionPO();
                    Optional<DeveloperProjectCollectionPO> projectOpt = developerProjectCollectionRepository.findByGitIdAndDeletedFalse((String) rep.get("id"));
                    if (projectOpt.isPresent()) {
                        developerProjectCollectionPO = projectOpt.get();
                    }
                    developerProjectCollectionPO.setDescription((String) rep.get("description"));
                    developerProjectCollectionPO.setGitId((String) rep.get("id"));
                    developerProjectCollectionPO.setUrl((String) rep.get("url"));
                    developerProjectCollectionPO.setName((String) rep.get("name"));
                    if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
                        developerProjectCollectionPO.setLanguage((String) ((Map) rep.get("primaryLanguage")).get("name"));
                    }
                    if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
                        developerProjectCollectionPO.setCommentsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
                    }
                    if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
                        developerProjectCollectionPO.setWatchersCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
                    }
                    if (!ObjectUtils.isEmpty(rep.get("issues"))) {
                        developerProjectCollectionPO.setIssuesCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
                    }
                    developerProjectCollectionPO.setStargazersCount((Integer) rep.get("stargazerCount"));
                    developerProjectCollectionPO.setDescription((String) rep.get("description"));
                    String projectId = developerProjectCollectionRepository.save(developerProjectCollectionPO).getId();
                    Optional<DeveloperAndProjectRelationShipCollectionPO> developerAndProjectRelationShipOpt = developerAndProjectRelationShipCollectionRepository.findByDeveloperIdAndProjectIdAndDeletedFalse(developerId, projectId);
                    DeveloperAndProjectRelationShipCollectionPO developerAndProjectRelationShipCollectionPO = new DeveloperAndProjectRelationShipCollectionPO();
                    if (developerAndProjectRelationShipOpt.isPresent()) {
                        developerAndProjectRelationShipCollectionPO = developerAndProjectRelationShipOpt.get();
                    }
                    developerAndProjectRelationShipCollectionPO.setDeveloperId(developerId);
                    developerAndProjectRelationShipCollectionPO.setProjectId(projectId);
                    developerAndProjectRelationShipCollectionPO.setPrimaryLanguage(developerProjectCollectionPO.getLanguage());
                    developerAndProjectRelationShipCollectionPO.setHasAnyRestrictedContributions((Boolean) contributionsCollectionMap.get("hasAnyRestrictedContributions"));
                    developerAndProjectRelationShipCollectionPO.setCommitCount((Integer) ((Map) commitRep.get("contributions")).get("totalCount"));
                    developerAndProjectRelationShipCollectionRepository.save(developerAndProjectRelationShipCollectionPO);
                }
        );

    }

    public List<DeveloperCollectionVO> findAll() {
        List<DeveloperCollectionPO> developerCollectionPOS = developerCollectionRepository.findAllByDeletedFalse();
        return developerFactory.toDeveloperCollectionVOList(developerCollectionPOS);
    }


    public DeveloperCollectionVO findByLogin(String login) {
        graphqlSearch(login);
        return developerFactory.toDeveloperCollectionVO(login);

    }
}
