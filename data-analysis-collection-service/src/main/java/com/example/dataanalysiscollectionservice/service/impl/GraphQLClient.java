package com.example.dataanalysiscollectionservice.service.impl;

import com.example.dataanalysiscollectionservice.pojo.entity.Developer;
import com.example.dataanalysiscollectionservice.repository.DeveloperAndProjectRelationShipRepository;
import com.example.dataanalysiscollectionservice.repository.DeveloperRepository;
import com.example.dataanalysiscollectionservice.repository.ProjectRepository;
import com.google.common.net.MediaType;

import okhttp3.RequestBody;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mountcloud.graphql.GraphqlClient;
import org.mountcloud.graphql.request.query.DefaultGraphqlQuery;
import org.mountcloud.graphql.request.query.GraphqlQuery;
import org.mountcloud.graphql.request.result.ResultAttributtes;
import org.mountcloud.graphql.response.GraphqlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class GraphQLClient {

    @Value("${git.token}")
    String GIT_TOKEN;
    @Value("${git.url}")
    String GIT_URL;
    String FILE_PATH = "D:\\javaProject1\\DataAnalysis\\data-analysis-collection-service\\src\\main\\resources\\query.graphql";

    @Autowired
    DeveloperRepository developerRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    DeveloperAndProjectRelationShipRepository developerAndProjectRelationShipRepository;

    public void graphqlSearch(String developerName){
        GraphqlClient graphqlClient = GraphqlClient.buildGraphqlClient(GIT_URL);
        Map<String,String> httpHeaders = new HashMap<>();
        httpHeaders.put("Authorization",GIT_TOKEN);
        graphqlClient.setHttpHeaders(httpHeaders);
        GraphqlQuery query = new DefaultGraphqlQuery("user");
        query.addParameter("login",developerName);
        query.addResultAttributes("id","avatarUrl","location","bio","isBountyHunter","isCampusExpert","isDeveloperProgramMember");
        ResultAttributtes contributionsCollection = new ResultAttributtes("contributionsCollection");
        contributionsCollection.addResultAttributes("hasAnyRestrictedContributions","totalCommitContributions","totalIssueContributions","totalPullRequestContributions","totalRepositoriesWithContributedCommits");
        ResultAttributtes pullRequestReviewContributionsByRepository = new ResultAttributtes("pullRequestReviewContributionsByRepository");
        ResultAttributtes primaryLanguage = new ResultAttributtes("primaryLanguage");
        ResultAttributtes repository = new ResultAttributtes("repository");
        repository.addResultAttributes("name","url");
        primaryLanguage.addResultAttributes("name");

        ResultAttributtes contributions = new ResultAttributtes("contributions");
        contributions.addResultAttributes("totalCount");
        query.addResultAttributes(contributionsCollection);
        pullRequestReviewContributionsByRepository.addResultAttributes(repository);
        contributionsCollection.addResultAttributes(pullRequestReviewContributionsByRepository);
        repository.addResultAttributes(primaryLanguage);

        ResultAttributtes issueContributionsByRepository = new ResultAttributtes("issueContributionsByRepository");
        issueContributionsByRepository.addResultAttributes(repository);
        contributionsCollection.addResultAttributes(issueContributionsByRepository);

        ResultAttributtes commitContributionsByRepository = new ResultAttributtes("commitContributionsByRepository");
        commitContributionsByRepository.addResultAttributes(repository);

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
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException("git请求失败:"+result.get("errors"));
        }
        Developer developer = new Developer();
        developer.setName(developerName);

        Map data  = (Map)result.get("data");
        Map user  = (Map)data.get("user");
        if(ObjectUtils.isEmpty(data.get("user"))){
            throw new RuntimeException("无此用户信息"+result.get("errors"));
        };
        if(developerRepository.findByGitIdAndDeletedFalse((String) user.get("id")).isPresent()){
            developer = developerRepository.findByGitIdAndDeletedFalse((String) user.get("id")).get();
        }
        developer.setBlo((String) user.get("bio"));
        developer.setAvatarUrl((String) user.get("avatarUrl"));
        developer.setGitId((String) user.get("id"));
        developer.setDeveloperProgramMemberFlag((Boolean)user.get("isDeveloperProgramMember"));
        developer.setBountyHunterFlag((Boolean) user.get("isBountyHunter"));
        developer.setCampusExpertFlag((Boolean) user.get("isCampusExpert"));
        Map followerMap =(Map) user.get("followers");
        developer.setFollowersCount((Integer) followerMap.get("totalCount"));
        Map gistMap = (Map)user.get("gists");
        developer.setPublicGistsCount((Integer) gistMap.get("totalCount"));
        Map repositoriesMap = (Map)user.get("repositories");
        developer.setPublicReposCount((Integer)repositoriesMap.get("totalCount"));

        String developerId =developerRepository.save(developer).getId();
        LinkedHashMap contributionsCollectionMap =(LinkedHashMap) user.get("contributionsCollection");
        contributionsCollectionMap.get("pullRequestReviewContributionsByRepository");
        //项目表如何更新



    }



    public void graphqlRequest(String developerName) {
        String query = "";
        try {
            query = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            query = query.replace("__DEVELOPER_NAME__", developerName);
        } catch (IOException e) {
            throw new RuntimeException("读取文件失败：", e);
        }
        System.out.println();
        String json = "{\"query\": \"" + query.replace("\"", "\\\"") + "\"}";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建HttpGet请求
            HttpPost httpPost = new HttpPost(GIT_URL);
            httpPost.setHeader("Authorization", GIT_TOKEN);
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // 处理响应
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String responseBody = EntityUtils.toString(responseEntity, "UTF-8");
                    System.out.println(responseBody);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
