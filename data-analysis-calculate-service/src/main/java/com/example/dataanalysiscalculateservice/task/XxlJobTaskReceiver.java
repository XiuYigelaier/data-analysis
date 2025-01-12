package com.example.dataanalysiscalculateservice.task;

import com.alibaba.fastjson.JSONObject;
import com.example.core.enums.ProjectClassificationEnum;
import com.example.dataanalysiscalculateservice.pojo.bo.DeveloperAreaBO;
import com.example.dataanalysiscalculateservice.pojo.bo.DeveloperNationAnswerBO;
import com.example.dataanalysiscalculateservice.pojo.bo.ProjectClassifyAnswerBO;
import com.example.dataanalysiscalculateservice.pojo.po.TalentRankPO;
import com.example.dataanalysiscalculateservice.pojo.po.TalentRankProjectPO;
import com.example.dataanalysiscalculateservice.repository.TalentRankProjectRepository;
import com.example.dataanalysiscalculateservice.repository.TalentRankRepository;
import com.example.dataanalysiscalculateservice.config.BigModelNew;
import com.example.dataanalysiscalculateservice.feign.CalculateClientFeign;
import com.example.dataanalysiscalculateservice.pojo.bo.CalculateDeveloperBO;
import com.example.dataanalysiscalculateservice.service.CalculateService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
@RabbitListener(queues = "queue.calculate")
public class XxlJobTaskReceiver {
    @Autowired
    CalculateClientFeign calculateClientFeign;
    private static final String NATION_FILE_PATH = "D:\\javaProject1\\DataAnalysis\\data-analysis-calculate-service\\src\\main\\resources\\NationRules.txt";
    private static final String AREA_FILE_PATH = "D:\\javaProject1\\DataAnalysis\\data-analysis-calculate-service\\src\\main\\resources\\AreaRules.txt";


    private static final String CLASSIFY_FILE_PATH=  "D:\\javaProject1\\DataAnalysis\\data-analysis-calculate-service\\src\\main\\resources\\ProjectClassification.txt";
    @Autowired
    TalentRankRepository talentRankRepository;

    @Autowired
    TalentRankProjectRepository talentRankProjectRepository;

    @Autowired
    CalculateService calculateService;

//    @RabbitHandler
//    public void receiver(Map message) throws Exception {
//        SecurityContextHolder ctx = new SecurityContextHolder();
//        Set<String> languages = new HashSet<>();
//        List<Double> historyScore = new ArrayList();
//        BigDecimal calculateSum = BigDecimal.ZERO;
//
//        Map data = (Map) message.get("data");
//        Map user = (Map) data.get("user");
//        TalentRankPO talentRank = new TalentRankPO();
//        Optional<TalentRankPO> talentRankOpt = talentRankRepository.findByGitIdAndDeletedFalse((String) user.get("id"));
//        if (talentRankOpt.isPresent()) {
//            talentRank = talentRankOpt.get();
//            if(StringUtils.hasText(talentRank.getScoreHistory())){
//                historyScore = JSONObject.parseArray(talentRank.getScoreHistory(), Double.class);
//            }
//            if(talentRankProjectRepository.findAllByTalentRankIdAndDeletedFalse(talentRank.getId()).isPresent()){
//                talentRankProjectRepository.deleteAllByTalentRankId(talentRank.getId());
//            }
//
//        }
//        talentRank.setBlo((String) user.get("bio"));
//        talentRank.setAvatarUrl((String) user.get("avatarUrl"));
//        talentRank.setGitId((String) user.get("id"));
//        talentRank.setName((String) user.get("name"));
//        talentRank.setLogin((String) message.get("login"));
//        CalculateDeveloperBO calculateDeveloperBO = new CalculateDeveloperBO();
//        Integer flagCount = 0;
//        flagCount = (Boolean) user.get("isDeveloperProgramMember") ? flagCount + 1 : flagCount;
//        flagCount = (Boolean) user.get("isBountyHunter") ? flagCount + 1 : flagCount;
//        flagCount = (Boolean) user.get("isCampusExpert") ? flagCount + 1 : flagCount;
//        Map followerMap = (Map) user.get("followers");
//        Map gistMap = (Map) user.get("gists");
//        LinkedHashMap repositoriesList = (LinkedHashMap) user.get("repositories");
//        calculateDeveloperBO.setGistCount((Integer) gistMap.get("totalCount")).setFlagCount(flagCount).setReposCount((Integer) repositoriesList.get("totalCount"))
//                .setFollowersCount((Integer) followerMap.get("totalCount"));
//
//        calculateSum = calculateService.calculateDeveloperParamContributions(calculateDeveloperBO).add(calculateSum);
//
//        LinkedHashMap contributionsCollectionMap = (LinkedHashMap) user.get("contributionsCollection");
//        ArrayList<LinkedHashMap> pullRequestReviewContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("pullRequestReviewContributionsByRepository");
//        ArrayList<LinkedHashMap> issueContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("issueContributionsByRepository");
//        ArrayList<LinkedHashMap> commitContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("commitContributionsByRepository");
//
//        for (LinkedHashMap pullRep : pullRequestReviewContributionsByRepositoryList) {
//            LinkedHashMap rep = (LinkedHashMap) pullRep.get("repository");
//            if (!ObjectUtils.isEmpty(rep.get("description"))){
//            }
//            if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
//                calculateDeveloperBO.setReposCommitsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
//            }
//            if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
//                calculateDeveloperBO.setReposWatcherCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
//            }
//            if (!ObjectUtils.isEmpty(rep.get("issues"))) {
//                calculateDeveloperBO.setReposStarCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
//            }
//            if (!ObjectUtils.isEmpty(pullRep.get("contributions"))) {
//                calculateDeveloperBO.setPullReviewForReposCount((Integer) ((Map) pullRep.get("contributions")).get("totalCount"));
//            }
//            calculateDeveloperBO.setReposStarCount((Integer) rep.get("stargazerCount"));
//            calculateSum = calculateService.calculateReposContributions(calculateDeveloperBO).add(calculateSum);
//            if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
//                languages.add((String) ((Map) rep.get("primaryLanguage")).get("name"));
//            }
//
//
//        }
//
//        for (LinkedHashMap issueRep : issueContributionsByRepositoryList) {
//            LinkedHashMap rep = (LinkedHashMap) issueRep.get("repository");
//            if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
//                calculateDeveloperBO.setReposCommitsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
//            }
//            if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
//                calculateDeveloperBO.setReposWatcherCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
//            }
//            if (!ObjectUtils.isEmpty(rep.get("issues"))) {
//                calculateDeveloperBO.setReposStarCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
//            }
//            if (!ObjectUtils.isEmpty(issueRep.get("contributions"))) {
//                calculateDeveloperBO.setIssuesForRepsCount((Integer) ((Map) issueRep.get("contributions")).get("totalCount"));
//            }
//            calculateSum = calculateService.calculateReposContributions(calculateDeveloperBO).add(calculateSum);
//            if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
//                languages.add((String) ((Map) rep.get("primaryLanguage")).get("name"));
//            }
//        }
//
//        for (LinkedHashMap commitRep : commitContributionsByRepositoryList) {
//            LinkedHashMap rep = (LinkedHashMap) commitRep.get("repository");
//            if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
//                calculateDeveloperBO.setReposCommitsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
//            }
//            if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
//                calculateDeveloperBO.setReposWatcherCount((Integer) ((Map) rep.get("watchers")).get("totalCount"));
//            }
//            if (!ObjectUtils.isEmpty(rep.get("issues"))) {
//                calculateDeveloperBO.setReposStarCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
//            }
//            if (!ObjectUtils.isEmpty(commitRep.get("contributions"))) {
//                calculateDeveloperBO.setCommitForReposCount((Integer) ((Map) commitRep.get("contributions")).get("totalCount"));
//            }
//            calculateSum = calculateService.calculateReposContributions(calculateDeveloperBO).add(calculateSum);
//            if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
//                languages.add((String) ((Map) rep.get("primaryLanguage")).get("name"));
//            }
//        }
//        calculateSum = calculateSum.setScale(2, RoundingMode.HALF_UP);
//        talentRank.setTalentRank(calculateSum);
//        historyScore.add(calculateSum.doubleValue());
//        talentRank.setScoreHistory( JSONObject.toJSONString(historyScore));
////暂时
//        talentRank.setLocation((String) user.get("location"));
//        StringBuilder stringBuilder = new StringBuilder();
//        for(String language :languages){
//            stringBuilder.append(language+",");
//        }
//
//        talentRank.setAreas(stringBuilder.toString());
////        //nation大模型推测
//        DeveloperNationAnswerBO developerNationAnswerBO = new DeveloperNationAnswerBO();
//        developerNationAnswerBO.setName((String) user.get("name"));
//        developerNationAnswerBO.setLocation((String) user.get("location"));
//        developerNationAnswerBO.setBio((String) user.get("bio"));
//        developerNationAnswerBO.setCompany((String) user.get("company"));
//        developerNationAnswerBO.setPronouns((String) user.get("pronouns"));
//        BigModelNew localModelNew = new BigModelNew(ctx.toString(), true);
//        byte[] nationBytes = Files.readAllBytes(Paths.get(NATION_FILE_PATH));
//        String localRule = new String(nationBytes, StandardCharsets.UTF_8);
//        CompletableFuture<String> localFuture = localModelNew.requestModel(localRule + developerNationAnswerBO);
//        //等待上次完成
//        if (!localFuture.isDone()) {
//            Thread.sleep(200);
//        }
//        String local = localFuture.get();
//        JSONObject localObj = calculateService.modelAnswerToJsonObject(local);
//        talentRank.setLocationCredence((String) localObj.get("credence"));
//        talentRank.setLocation((String) localObj.get("nation"));
//
//        //调用大模型获取擅长领域
//        BigModelNew areaModelNew = new BigModelNew(ctx.toString(), true);
//        DeveloperAreaBO developerAreaBO = new DeveloperAreaBO();
//        System.out.println("language:" + languages);
//        developerAreaBO.setLanguages(languages);
//        developerAreaBO.setBlo((String) user.get("bio"));
//        byte[] areaBytes = Files.readAllBytes(Paths.get(AREA_FILE_PATH));
//        String areaRule = new String(areaBytes, StandardCharsets.UTF_8);
//        CompletableFuture<String> areaFuture = areaModelNew.requestModel(areaRule + developerAreaBO);
//        JSONObject areaObj = calculateService.modelAnswerToJsonObject(areaFuture.get());
//        talentRank.setAreas((String) areaObj.get("area"));
//        talentRank.setAreaCredence((String) areaObj.get("credence"));
//        TalentRankPO talentRankPO = talentRankRepository.save(talentRank);
//        System.out.println(talentRankPO);
//        String talentRankId = talentRankPO.getId();
//
//
//        ArrayList<LinkedHashMap> nodes = (ArrayList) repositoriesList.get("nodes");
//        for(LinkedHashMap rep:nodes){
//            ProjectClassifyAnswerBO projectClassifyAnswerBO = new ProjectClassifyAnswerBO();
//            TalentRankProjectPO repository = new TalentRankProjectPO();
//            repository.setName((String) rep.get("name"));
//            projectClassifyAnswerBO.setProjectName((String) rep.get("name"));
//            repository.setUrl((String) rep.get("url"));
//            repository.setStarCount((Integer) rep.get("stargazerCount"));
//            repository.setTalentRankId(talentRankId);
//            repository.setDescription((String) rep.get("description"));
//            projectClassifyAnswerBO.setDescription((String) rep.get("description"));
//            BigModelNew bigModelNew = new BigModelNew(ctx.toString(),true);
//            byte[] classifyBytes = Files.readAllBytes(Paths.get(CLASSIFY_FILE_PATH));
//            String classifyRule = new String(classifyBytes, StandardCharsets.UTF_8);
//            CompletableFuture<String> classifyFuture = localModelNew.requestModel(classifyRule + projectClassifyAnswerBO);
//            //等待上次完成
//            if (!classifyFuture.isDone()) {
//                Thread.sleep(200);
//            }
//            String classify = classifyFuture.get();
//            repository.setClassification(ProjectClassificationEnum.valueOf(classify));
//            talentRankProjectRepository.save(repository);
//
//        }
//
//    }


}
