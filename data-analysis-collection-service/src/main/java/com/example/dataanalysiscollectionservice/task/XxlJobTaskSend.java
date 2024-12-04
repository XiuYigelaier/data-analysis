package com.example.dataanalysiscollectionservice.task;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.dataanalysiscollectionservice.service.impl.GraphQLSearchServiceImpl;
import com.xxl.job.core.handler.annotation.XxlJob;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class XxlJobTaskSend {
    @Value("1000")
    String GITHUB_SEARCH_FOLLOWERS_MIN;
    @Value("50")
    String GITHUB_SEARCH_PER_PAGE;
    @Value("${git.token}")
    String GIT_TOKEN;
    static Integer page = 1;
    private static final String GITHUB_SEARCH_USERS_URL = "https://api.github.com/search/users";
    private static final OkHttpClient client = new OkHttpClient();
    @Autowired
    GraphQLSearchServiceImpl graphQLSearchService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @XxlJob("searchDeveloperHandler")
    public void scheduledSearchUser() throws IOException {

        HttpUrl urlBuilder = HttpUrl.parse(GITHUB_SEARCH_USERS_URL)
                .newBuilder()
                .addQueryParameter("q", "followers:>" + GITHUB_SEARCH_FOLLOWERS_MIN)
                .addQueryParameter("sort", "followers")
                .addQueryParameter("order", "desc")
                .addQueryParameter("per_page", GITHUB_SEARCH_PER_PAGE)
                .addQueryParameter("page", page.toString())
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder)
                .addHeader("Authorization", GIT_TOKEN)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                // 请求成功，处理响应
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                Integer totalCount =  jsonObject.getInteger("total_count");
                Integer pageNum = totalCount / page;
                if (page >= pageNum) {
                    page = 0;
                } else {
                    page++;
                }
                List<String> loginList = new ArrayList<>();
                JSONArray itemArray =  jsonObject.getJSONArray("items");
                itemArray.forEach(
                        item -> {
                            String login =((JSONObject)item).getString("login");
                            loginList.add(login);
                            Map searchMap = graphQLSearchService.graphqlSearch(login);
                            rabbitTemplate.convertAndSend("queue.calculate",searchMap);
                        }
                );
            } else {
                // 请求失败，处理错误
                System.err.println("Request failed: " + response.code());
            }


        }
    }

}
