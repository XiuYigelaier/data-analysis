package com.example.dataanalysiscalculateservice.task;

import com.alibaba.fastjson.JSONObject;
import com.example.core.entity.*;
import com.example.core.repository.TalentRankRepository;
import com.example.dataanalysiscalculateservice.config.BigModelNew;
import com.example.dataanalysiscalculateservice.feign.CalculateClientFeign;
import com.example.dataanalysiscalculateservice.pojo.CalculateEntity;
import com.example.dataanalysiscalculateservice.service.CalculateService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RabbitListener(queues = "queue.calculate")
public class XxlJobTaskReceiver {
    @Autowired
    CalculateClientFeign calculateClientFeign;
    private static final String NATION_FILE_PATH = "D:\\javaProject1\\DataAnalysis\\data-analysis-calculate-service\\src\\main\\resources\\NationRules.txt";
    private static final String AREA_FILE_PATH = "D:\\javaProject1\\DataAnalysis\\data-analysis-calculate-service\\src\\main\\resources\\AreaRules.txt";

    @Autowired
    TalentRankRepository talentRankRepository;

    @Autowired
    CalculateService calculateService;

    @RabbitHandler
    public void receiver(Map message) throws Exception {
        Set<String> languages = new HashSet<>();
        BigDecimal calculateSum = BigDecimal.ZERO;
        Map data = (Map) message.get("data");
        Map user = (Map) data.get("user");
        TalentRank talentRank = new TalentRank();
        Optional<TalentRank> talentRankOpt = talentRankRepository.findByGitIdAndDeletedFalse((String) user.get("id"));
        if (talentRankOpt.isPresent()) {
            talentRank = talentRankOpt.get();
        }
        talentRank.setBlo((String) user.get("bio"));
        talentRank.setAvatarUrl((String) user.get("avatarUrl"));
        talentRank.setGitId((String) user.get("id"));
        talentRank.setName((String) user.get("name"));
        talentRank.setLogin((String) message.get("login"));
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

        calculateSum = calculateService.calculateDeveloperParamContributions(calculateEntity).add(calculateSum);

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
            calculateSum = calculateService.calculateReposContributions(calculateEntity).add(calculateSum);
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
            calculateSum = calculateService.calculateReposContributions(calculateEntity).add(calculateSum);
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
            calculateSum = calculateService.calculateReposContributions(calculateEntity).add(calculateSum);
            if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
                languages.add((String) ((Map) rep.get("primaryLanguage")).get("name"));
            }
        }
        talentRank.setTalentRank(calculateSum);


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
        JSONObject localObj = calculateService.modelAnswerToJsonObject(local);
        talentRank.setLocationCredence((String) localObj.get("credence"));
        talentRank.setLocation((String) localObj.get("nation"));

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
        JSONObject areaObj = calculateService.modelAnswerToJsonObject(areaFuture.get());
        talentRank.setAreas((String) areaObj.get("area"));
        talentRank.setAreaCredence((String) areaObj.get("credence"));
        talentRankRepository.save(talentRank);

    }


}
