package com.example.dataanalysiscollectionservice.service.impl;

import com.example.core.repository.mysql.DeveloperAndProjectRelationShipRepository;
import com.example.core.repository.mysql.DeveloperRepository;
import com.example.core.repository.mysql.ProjectRepository;
import com.example.core.service.AsyncSave;
import com.example.core.service.GraphQLSearchService;


import okhttp3.OkHttpClient;
import org.mountcloud.graphql.GraphqlClient;
import org.mountcloud.graphql.request.query.DefaultGraphqlQuery;
import org.mountcloud.graphql.request.query.GraphqlQuery;

import org.mountcloud.graphql.request.result.ResultAttributtes;
import org.mountcloud.graphql.response.GraphqlResponse;
import org.springframework.beans.factory.FactoryBean;
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
    @Value("1000")
    String GITHUB_SEARCH_FOLLOWERS_MIN;
    @Value("50")
    String GITHUB_SEARCH_PER_PAGE;
    private static final String GITHUB_SEARCH_USERS_URL = "https://api.github.com/search/users";

    private static final String FILE_PATH = "D:\\javaProject1\\DataAnalysis\\data-analysis-collection-service\\src\\main\\resources\\query.graphql";

    private static final OkHttpClient client = new OkHttpClient();
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
        ResultAttributtes nodes =  new ResultAttributtes("nodes");
        nodes.addResultAttributes("name","url","stargazerCount");
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
        followingNode.addResultAttributes("login","id","avatarUrl");
        following.addResultAttributes(followingNode);
        query.addResultAttributes(following);

//        pullRequestReviewContributionsByRepository.addResultAttributes(description);
//        issueContributionsByRepository.addResultAttributes(description);
//        commitContributionsByRepository.addResultAttributes(description);



        Map result = null;
        try {
            GraphqlResponse response = graphqlClient.doQuery(query);
            result = response.getData();

        } catch (IOException e) {
            throw new RuntimeException("git请求失败:" + result.get("errors"));
        }
        if(ObjectUtils.isEmpty(result)){
            throw new  RuntimeException("无result：网络错误");
        }
//        if(ObjectUtils.isEmpty(result.get("user"))){
//            //无法访问这个用户
//            return  null;
//        }
        Map data = (Map)result.get("data");
       if(!ObjectUtils.isEmpty(data.get("user"))){
           asyncSave.save(developerName, result);
       }
        return result;

    }

//    @XxlJob("searchDeveloperHandler")
//    public void scheduledSearchUser() throws IOException {
//
//        HttpUrl urlBuilder = HttpUrl.parse(GITHUB_SEARCH_USERS_URL)
//                .newBuilder()
//                .addQueryParameter("q", "followers:>" + GITHUB_SEARCH_FOLLOWERS_MIN)
//                .addQueryParameter("sort", "followers")
//                .addQueryParameter("order", "desc")
//                .addQueryParameter("per_page", GITHUB_SEARCH_PER_PAGE)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(urlBuilder)
//                .addHeader("Authorization", GIT_TOKEN)
//                .build();
//
//          try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful()) {
//                // 请求成功，处理响应
//                System.out.println(response.body().string());
//            } else {
//                // 请求失败，处理错误
//                System.err.println("Request failed: " + response.code());
//            }
//        }
//    }


}
