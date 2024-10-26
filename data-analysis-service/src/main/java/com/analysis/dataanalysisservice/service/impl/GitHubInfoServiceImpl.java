package com.analysis.dataanalysisservice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.analysis.dataanalysisservice.pojo.entity.Developer;
import com.analysis.dataanalysisservice.pojo.entity.PageRankEntity;
import com.analysis.dataanalysisservice.repository.DeveloperRepository;
import com.analysis.dataanalysisservice.service.GitHubInfoService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GitHubInfoServiceImpl implements GitHubInfoService {

    @Value("${git.token}")
    private String gitToken;

    @Autowired
    DeveloperRepository developerRepository;

    @Override
    public List<String> getRepos(String projectUser) {
        List<String> reposList = new ArrayList<>();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 GET 请求
            HttpGet request = new HttpGet("https://api.github.com/users/" + projectUser + "/repos");
            request.addHeader("Accept", "application/vnd.github.v3+json");
            request.addHeader("Authorization", gitToken);

            // 执行请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String result = EntityUtils.toString(response.getEntity());
              //  System.out.println("Response: " + result);
                JSONArray jsonArray = JSONArray.parseArray(result);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    reposList.add((String) jsonObject.get("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(projectUser + "获取仓库失败", e);
        }
        return reposList;

    }

    @Override
    public Object calculateContribution(String projectUser, String repos) {
        List<Developer> developerList = new ArrayList<>();
        Map<String,Long> actionsMap = getActions(projectUser,repos);
        Map<String,Long> commitsMap = getCommits(projectUser,repos);
        Map<String,Long> issuesMap = getIssues( projectUser, repos);
        List<JSONObject> userInfoJsonObject = new ArrayList<>();
        List<String> userNameList = new ArrayList<>();
        List<PageRankEntity> pageRankEntityList = new ArrayList<>();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 GET 请求
            HttpGet request = new HttpGet("https://api.github.com/repos/" + projectUser + "/" + repos + "/contributors");
            request.addHeader("Accept", "application/vnd.github.v3+json");
            request.addHeader("Authorization", gitToken);
            // 执行请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String result = EntityUtils.toString(response.getEntity());
               // System.out.println("Response: " + result);
                JSONArray contributorUsers = JSONArray.parseArray(result);
                for (int i = 0; i < contributorUsers.size(); i++) {
                    JSONObject contributorUsersJSONObject = contributorUsers.getJSONObject(i);
                    String loginUser = (String) contributorUsersJSONObject.get("login");
                    userNameList.add(loginUser);
                    //通过贡献列表获得其中用户具体信息
                    userInfoJsonObject.add(userInfo(loginUser));
                    Integer loginContributions = (Integer) contributorUsersJSONObject.get("contributions");

                    Developer developer = new Developer();
                    if(developerRepository.findByNameAndDeletedFalse(loginUser).isPresent()){
                        developer = developerRepository.findByNameAndDeletedFalse(loginUser).get();
                    }
                    developer.setName(loginUser);
                    developer.setActionsNum(actionsMap.getOrDefault(loginUser,0L));
                    developer.setCommitsNum(commitsMap.getOrDefault(loginUser,0L));
                    developer.setIssuesNum(issuesMap.getOrDefault(loginUser,0L));
                    developer.setContribute(loginContributions);
                    developerList.add(developer);
                }
                developerRepository.saveAll(developerList);
                pageRankEntityList = pageRankConvert(projectUser, userNameList);


            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(projectUser + "获取仓库失败", e);
        }
        return null;
    }

    public Map<String, Long> getActions(String projectUser, String project) {
        JSONObject userInfoJsonObject = null;
        Map<String, Long> actionsMap = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 GET 请求
            HttpGet request = new HttpGet("https://api.github.com/repos/" + projectUser + "/" + project + "/actions/runs");
            request.addHeader("Accept", "application/vnd.github.v3+json");
            request.addHeader("Authorization", gitToken);
            // 执行请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String result = EntityUtils.toString(response.getEntity());
              //  System.out.println("Response: " + result);
                JSONArray issuesArray =  (JSONArray) JSONObject.parseObject(result).get("workflow_runs");
              //  String workFlow_runs =  (String) JSONObject.parseObject(result).get("workflow_runs");
               // JSONArray issuesArray = JSONArray.parseArray(workFlow_runs);
                actionsMap = issuesArray.stream()
                        .collect(Collectors.groupingBy(
                                actions -> {
                                    JSONObject author = ((JSONObject) actions).getJSONObject("actor"); // 获取嵌套的author JSONObject
                                    return author.getString("login"); // 从author JSONObject中提取id字段
                                }, // 使用getString方法获取author字段
                                Collectors.counting() // 计算每个作者的issues数量
                        ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return actionsMap;
    }

    public Map<String, Long> getIssues(String projectUser, String project) {
        JSONObject userInfoJsonObject = null;
        Map<String, Long> issuesMap = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 GET 请求
            HttpGet request = new HttpGet("https://api.github.com/repos/" + projectUser + "/" + project + "/issues");
            request.addHeader("Accept", "application/vnd.github.v3+json");
            request.addHeader("Authorization", gitToken);
            // 执行请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String result = EntityUtils.toString(response.getEntity());
                JSONArray issuesArray = JSONArray.parseArray(result);
                issuesMap = issuesArray.stream()
                        .collect(Collectors.groupingBy(
                                issues -> {
                                    JSONObject author = ((JSONObject) issues).getJSONObject("user"); // 获取嵌套的author JSONObject
                                    return author.getString("login"); // 从author JSONObject中提取id字段
                                }, // 使用getString方法获取author字段
                                Collectors.counting() // 计算每个作者的issues数量
                        ));
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return issuesMap;
    }

    public Map<String,Long> getCommits(String projectUser, String project) {
        JSONObject userInfoJsonObject = null;
        Map<String, Long> commitsMap = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 GET 请求
            HttpGet request = new HttpGet("https://api.github.com/repos/" + projectUser + "/" + project + "/commits");
            request.addHeader("Accept", "application/vnd.github.v3+json");
            request.addHeader("Authorization", gitToken);
            // 执行请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String result = EntityUtils.toString(response.getEntity());
                //   System.out.println("Response: " + result);
                JSONArray issuesArray = JSONArray.parseArray(result);
                commitsMap = issuesArray.stream()
                        .collect(Collectors.groupingBy(
                                commits -> {
                                    JSONObject author = ((JSONObject) commits).getJSONObject("author"); // 获取嵌套的author JSONObject
                                    return author.getString("login"); // 从author JSONObject中提取id字段
                                }, // 使用getString方法获取author字段
                                Collectors.counting() // 计算每个作者的issues数量
                        ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return commitsMap;
    }


    public JSONObject userInfo(String projectUser) {
        JSONObject userInfoJsonObject = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 GET 请求
            HttpGet request = new HttpGet("https://api.github.com/users/" + projectUser);
            request.addHeader("Accept", "application/vnd.github.v3+json");
            request.addHeader("Authorization", gitToken);
            // 执行请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String result = EntityUtils.toString(response.getEntity());
             //   System.out.println("Response: " + result);
                userInfoJsonObject = JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfoJsonObject;
    }


    public List<PageRankEntity> pageRankConvert(String projectUser, List<String> usersList) {
        List<PageRankEntity> pageRankEntityList = new ArrayList<>();
        for (int i = 0; i < usersList.size(); i++) {
            PageRankEntity pageRankEntity = new PageRankEntity(usersList.get(i), projectUser);
            pageRankEntityList.add(pageRankEntity);

        }
        return pageRankEntityList;
    }
}
