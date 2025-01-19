package com.example.dataanalysiscollectionservice.task;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.example.core.pojo.dto.DeveloperCollectionTranDTO;
import com.example.core.pojo.dto.DeveloperProjectCollectionTranDTO;
import com.example.dataanalysiscollectionservice.pojo.vo.mysql.DeveloperCollectionVO;
import com.example.dataanalysiscollectionservice.service.impl.GraphQLSearchServiceImpl;
import com.xxl.job.core.handler.annotation.XxlJob;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class XxlJobTaskSend {
    @Value("500")
    String GITHUB_SEARCH_FOLLOWERS_MIN;
    @Value("100")
    String GITHUB_SEARCH_PER_PAGE;
    @Value("${git.token}")
    String GIT_TOKEN;
    static Integer page = 0;
    private static final String GITHUB_SEARCH_USERS_URL = "https://api.github.com/search/users";
    private static final OkHttpClient client = new OkHttpClient();
    @Autowired
    GraphQLSearchServiceImpl graphQLSearchService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    boolean hasMoreDate = true;

    private static final Logger log = LoggerFactory.getLogger(XxlJobTaskSend.class);


    @XxlJob("searchDeveloperHandler")
    public void scheduledSearchUser() throws IOException {
        System.out.println(GIT_TOKEN);
        while (hasMoreDate&&page<=10) {
            HttpUrl urlBuilder = HttpUrl.parse(GITHUB_SEARCH_USERS_URL)
                    .newBuilder()
                    .addQueryParameter("q", "followers:>" + GITHUB_SEARCH_FOLLOWERS_MIN)
                    .addQueryParameter("sort", "followers")
                    .addQueryParameter("order", "desc")
                    .addQueryParameter("per_page", GITHUB_SEARCH_PER_PAGE)
                    .addQueryParameter("page", String.valueOf(page))
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
                    Integer totalCount = jsonObject.getInteger("total_count");

                    List<String> loginList = new ArrayList<>();
                    JSONArray itemArray = jsonObject.getJSONArray("items");
                    if (itemArray.isEmpty()) {
                        hasMoreDate = false;
                    }
                    itemArray.forEach(
                            item -> {
                                String login = ((JSONObject) item).getString("login");
                                log.info("定时任务搜索当前github用户【"+login+"】");
                                loginList.add(login);
                                graphQLSearchService.graphqlSearch(login);
                                DeveloperCollectionVO developerCollectionVO = graphQLSearchService.findByLogin(login);
                                DeveloperCollectionTranDTO developerCollectionTranDTO = new DeveloperCollectionTranDTO();
                                BeanUtils.copyProperties(developerCollectionVO, developerCollectionTranDTO);
                                List<DeveloperProjectCollectionTranDTO> developerProjectCollectionTranDTOList = new ArrayList<>();
                                developerCollectionVO.getDeveloperProjectCollectionList().forEach(
                                        developerProject -> {
                                            DeveloperProjectCollectionTranDTO developerProjectCollectionTranDTO = new DeveloperProjectCollectionTranDTO();
                                            BeanUtils.copyProperties(developerProject, developerProjectCollectionTranDTO);
                                            developerProjectCollectionTranDTOList.add(developerProjectCollectionTranDTO);
                                        }
                                );
                                developerCollectionTranDTO.setDeveloperProjectCollectionList(developerProjectCollectionTranDTOList);
                                rabbitTemplate.convertAndSend("queue.calculate", JSON.toJSONString(developerCollectionTranDTO));
                            }
                    );
                } else {
                    // 请求失败，处理错误
                    log.error("定时任务请求失败【"+response.message()+"】");
                    System.err.println("Request failed: " + response.code()+":"+response.message());
                }


            }
            page++;
            System.out.println(page + "------------------------");


        }
    }

}
