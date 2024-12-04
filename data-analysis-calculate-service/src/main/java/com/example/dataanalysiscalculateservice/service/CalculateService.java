package com.example.dataanalysiscalculateservice.service;

import com.alibaba.fastjson.JSONObject;
import com.example.core.entity.*;
import com.example.dataanalysiscalculateservice.config.BigModelNew;
import com.example.dataanalysiscalculateservice.feign.CalculateClientFeign;
import com.example.dataanalysiscalculateservice.pojo.CalculateEntity;
import com.xxl.job.core.handler.annotation.XxlJob;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CalculateService {
    @Autowired
    CalculateClientFeign calculateClientFeign;
    private static final String NATION_FILE_PATH = "D:\\javaProject1\\DataAnalysis\\data-analysis-calculate-service\\src\\main\\resources\\NationRules";
    private static final  String AREA_FILE_PATH = "D:\\javaProject1\\DataAnalysis\\data-analysis-calculate-service\\src\\main\\resources\\AreaRules";

    public TalentRankTrans calculate(String login) throws Exception {

        Set<String> languages = new HashSet<>();
        BigDecimal calculateSum = BigDecimal.ZERO;
        ResponseModel<?> responseModel = calculateClientFeign.graphqlSearch(login);
        if (!responseModel.isSuccess()) {
            throw new RuntimeException(responseModel.getMessageInfo());
        }
        Map result = (Map) responseModel.getData();
        Map data = (Map) result.get("data");

        Map user = (Map) data.get("user");
        if (ObjectUtils.isEmpty(data.get("user"))) {
            return  null;
//            throw new RuntimeException("无此用户信息" + result.get("errors"));
        }
        ;
        TalentRankTrans talentRankTrans = new TalentRankTrans();
        talentRankTrans.setBlo((String) user.get("bio"));
        talentRankTrans.setAvatarUrl((String) user.get("avatarUrl"));
        talentRankTrans.setGitId((String) user.get("id"));
        talentRankTrans.setName((String) user.get("name"));
        talentRankTrans.setLogin(login);
        CalculateEntity calculateEntity = new CalculateEntity();
        Integer flagCount = 0;
        flagCount = (Boolean) user.get("isDeveloperProgramMember") ? flagCount + 1 : flagCount;
        flagCount = (Boolean) user.get("isBountyHunter") ? flagCount + 1 : flagCount;
        flagCount = (Boolean) user.get("isCampusExpert") ? flagCount + 1 : flagCount;
        Map followerMap = (Map) user.get("followers");
        Map gistMap = (Map) user.get("gists");
        LinkedHashMap repositoriesList = (LinkedHashMap) user.get("repositories");
        calculateEntity.setGistCount((Integer) gistMap.get("totalCount")).setFlagCount(flagCount).setReposCount((Integer) repositoriesList.get("totalCount"))
                .setFollowersCount((Integer) followerMap.get("totalCount"));

        LinkedHashMap contributionsCollectionMap = (LinkedHashMap) user.get("contributionsCollection");
        ArrayList<LinkedHashMap> pullRequestReviewContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("pullRequestReviewContributionsByRepository");
        ArrayList<LinkedHashMap> issueContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("issueContributionsByRepository");
        ArrayList<LinkedHashMap> commitContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("commitContributionsByRepository");

        for (LinkedHashMap pullRep : pullRequestReviewContributionsByRepositoryList) {
            LinkedHashMap rep = (LinkedHashMap) pullRep.get("repository");
            if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
                calculateEntity.setReposCommitsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
            }
            if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
                calculateEntity.setReposWatcherCounts((Integer) ((Map) rep.get("watchers")).get("totalCount"));
            }
            if (!ObjectUtils.isEmpty(rep.get("issues"))) {
                calculateEntity.setReposStarCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
            }
            if (!ObjectUtils.isEmpty(pullRep.get("contributions"))) {
                calculateEntity.setPullReviewForReposCount((Integer) ((Map) pullRep.get("contributions")).get("totalCount"));
            }
            calculateEntity.setReposStarCount((Integer) rep.get("stargazerCount"));
            calculateSum = calculateContributions(calculateEntity).add(calculateSum);
            if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
                languages.add((String) ((Map) rep.get("primaryLanguage")).get("name"));
            }


        }

        for (LinkedHashMap issueRep : issueContributionsByRepositoryList) {
            LinkedHashMap rep = (LinkedHashMap) issueRep.get("repository");
            if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
                calculateEntity.setReposCommitsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
            }
            if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
                calculateEntity.setReposWatcherCounts((Integer) ((Map) rep.get("watchers")).get("totalCount"));
            }
            if (!ObjectUtils.isEmpty(rep.get("issues"))) {
                calculateEntity.setReposStarCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
            }
            if (!ObjectUtils.isEmpty(issueRep.get("contributions"))) {
                calculateEntity.setIssuesForRepsCount((Integer) ((Map) issueRep.get("contributions")).get("totalCount"));
            }
            calculateSum = calculateContributions(calculateEntity).add(calculateSum);
            if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
                languages.add((String) ((Map) rep.get("primaryLanguage")).get("name"));
            }
        }

        for (LinkedHashMap commitRep : commitContributionsByRepositoryList) {
            LinkedHashMap rep = (LinkedHashMap) commitRep.get("repository");
            if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
                calculateEntity.setReposCommitsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
            }
            if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
                calculateEntity.setReposWatcherCounts((Integer) ((Map) rep.get("watchers")).get("totalCount"));
            }
            if (!ObjectUtils.isEmpty(rep.get("issues"))) {
                calculateEntity.setReposStarCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
            }
            if (!ObjectUtils.isEmpty(commitRep.get("contributions"))) {
                calculateEntity.setCommitForReposCount((Integer) ((Map) commitRep.get("contributions")).get("totalCount"));
            }
            calculateSum = calculateContributions(calculateEntity).add(calculateSum);
            if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
                languages.add((String) ((Map) rep.get("primaryLanguage")).get("name"));
            }
        }
        talentRankTrans.setRank(calculateSum);


        //nation大模型推测
        DeveloperNation developerNation = new DeveloperNation();
        developerNation.setName((String) user.get("name"));
        developerNation.setLocation((String) user.get("location"));
        developerNation.setBio((String) user.get("bio"));
        developerNation.setCompany((String) user.get("company"));
        developerNation.setPronouns((String) user.get("pronouns"));
        SecurityContextHolder ctx = new SecurityContextHolder();
        BigModelNew localModelNew = new BigModelNew(ctx.toString(), true);
        byte[] nationBytes = Files.readAllBytes(Paths.get(NATION_FILE_PATH));
        String localRule = new String(nationBytes, StandardCharsets.UTF_8);
        CompletableFuture<String> localFuture = localModelNew.requestModel(localRule + developerNation);
        //等待上次完成
        if (!localFuture.isDone()) {
            Thread.sleep(200);
        }
        String local = localFuture.get();
        JSONObject localObj = modelAnswerToJsonObject(local);
        talentRankTrans.setLocationCredence((String) localObj.get("credence"));
        talentRankTrans.setLocation((String) localObj.get("nation"));

        //调用大模型获取擅长领域
        BigModelNew areaModelNew = new BigModelNew(ctx.toString(), true);
        DeveloperArea developerArea = new DeveloperArea();
        AreaAnswerModel areaAnswerModel = new AreaAnswerModel();
        System.out.println("language:" + languages);
        developerArea.setLanguages(languages);
        developerArea.setBlo((String) user.get("bio"));
        byte[] areaBytes = Files.readAllBytes(Paths.get(AREA_FILE_PATH));
        String areaRule = new String(areaBytes, StandardCharsets.UTF_8);
        CompletableFuture<String> areaFuture = areaModelNew.requestModel(areaRule + developerArea);
        JSONObject areaObj = modelAnswerToJsonObject(areaFuture.get());
        talentRankTrans.setAreas((String) areaObj.get("area"));
        talentRankTrans.setAreaCredence((String) areaObj.get("credence"));
        return talentRankTrans;

    }

    public JSONObject modelAnswerToJsonObject(String modelAnswer) {
        String regex = "```json(.*?)```";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL); // DOTALL 使得 . 可以匹配换行符
        Matcher matcher = pattern.matcher(modelAnswer);
        if (matcher.find()) {
            String jsonContent = matcher.group(1);
            JSONObject jsonObject = JSONObject.parseObject(jsonContent);
            System.out.println("提取的 JSON 内容是：" + jsonContent);
            return jsonObject;
        } else {
            System.out.println("未找到匹配的 JSON 内容。");
            throw new RuntimeException("未找到匹配的 JSON 内容。");
        }

    }


    public BigDecimal calculateContributions(CalculateEntity entity) {
        BigDecimal developerParam = BigDecimal.valueOf(Math.log(entity.getFollowersCount()) + 0.1 * entity.getReposCount() + 0.1 * entity.getGistCount() + 0.1 * entity.getFlagCount());
        BigDecimal developerForReposParam = BigDecimal.valueOf(0.3 * entity.getPullReviewForReposCount() + 0.6 * entity.getCommitForReposCount() + 0.1 * entity.getIssuesForRepsCount());
        BigDecimal reposParam = BigDecimal.valueOf(0.1 * entity.getReposCommitsCount() + 0.2 * entity.getReposIssuesCount() + 0.3 * entity.getReposWatcherCounts() + 0.4 * entity.getReposStarCount());
        return developerParam.multiply(developerForReposParam.multiply(reposParam));
    }




}
