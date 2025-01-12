package com.example.dataanalysiscalculateservice.service;

import com.alibaba.fastjson.JSONObject;
import com.example.core.pojo.base.ResponseModel;
import com.example.core.repository.neo4j.DeveloperGraphRepository;
import com.example.dataanalysiscalculateservice.config.BigModelNew;
import com.example.dataanalysiscalculateservice.feign.CalculateClientFeign;
import com.example.dataanalysiscalculateservice.pojo.bo.*;
import com.example.dataanalysiscalculateservice.pojo.dto.DeveloperCollectionDTO;
import com.example.dataanalysiscalculateservice.pojo.dto.DeveloperProjectCollectionDTO;
import com.example.dataanalysiscalculateservice.pojo.po.ScoreHistoryPO;
import com.example.dataanalysiscalculateservice.pojo.po.TalentRankPO;
import com.example.dataanalysiscalculateservice.pojo.po.TalentRankProjectPO;
import com.example.dataanalysiscalculateservice.pojo.vo.ScoreHistoryVO;
import com.example.dataanalysiscalculateservice.pojo.vo.TalentRankProjectVO;
import com.example.dataanalysiscalculateservice.pojo.vo.TalentRankVO;
import com.example.dataanalysiscalculateservice.repository.ScoreHistoryRepository;
import com.example.dataanalysiscalculateservice.repository.TalentRankProjectRepository;
import com.example.dataanalysiscalculateservice.repository.TalentRankRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CalculateService {
    @Autowired
    CalculateClientFeign calculateClientFeign;
    @Autowired
    ScoreHistoryRepository scoreHistoryRepository;
    @Autowired
    DeveloperGraphRepository developerGraphRepository;
    @Autowired
    private TalentRankRepository talentRankRepository;
    @Autowired
    private TalentRankProjectRepository talentRankProjectRepository;

    private static final String NATION_FILE_PATH = "/Users/xiuyi/Documents/project/DATA-ANALYSIS/DataAnalysis/data-analysis-calculate-service/src/main/resources/AreaRules.txt";
    private static final String AREA_FILE_PATH = "/Users/xiuyi/Documents/project/DATA-ANALYSIS/DataAnalysis/data-analysis-calculate-service/src/main/resources/NationRules.txt";
    private static final String PROJECT_CATEGORY_PATH = "/Users/xiuyi/Documents/project/DATA-ANALYSIS/DataAnalysis/data-analysis-calculate-service/src/main/resources/ProjectClassification.txt";


    public void calculateAll() {
        ResponseModel<List<DeveloperCollectionDTO>> responseModel = calculateClientFeign.searchAllDeveloper();
        if (!responseModel.isSuccess()) {
            throw new RuntimeException(responseModel.getMessageInfo());
        }
        responseModel.getData().forEach(
                developerCollectionDTO -> {
                    try {
                        calculate(developerCollectionDTO);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
        );
    }

    public void calculateByLogin(String login) throws Exception {
        ResponseModel<DeveloperCollectionDTO> responseModel = calculateClientFeign.findByProjectUser(login);
        if (!responseModel.isSuccess()) {
            throw new RuntimeException(responseModel.getMessageInfo());
        }

        calculate(responseModel.getData());

    }


    public void calculate(DeveloperCollectionDTO developerCollectionDTO) throws Exception {
        String gitId = developerCollectionDTO.getGitId();
        SecurityContextHolder ctx = new SecurityContextHolder();
        TalentRankPO talentRankPO = talentRankRepository.findByGitIdAndDeletedFalse(gitId).orElseGet(TalentRankPO::new);
        if (StringUtils.hasText(talentRankPO.getId())) {
            talentRankProjectRepository.deleteAllByDeveloperIdAndDeletedFalse(talentRankPO.getId());

        }
        BigDecimal totalScore = BigDecimal.ZERO;
        CalculateDeveloperBO calculateDeveloperBO = new CalculateDeveloperBO();
        calculateDeveloperBO.setGistCount(developerCollectionDTO.getPublicGistsCount());
        Integer flagCount = 0;
        flagCount = developerCollectionDTO.getDeveloperProgramMemberFlag() ? flagCount + 1 : flagCount;
        flagCount = developerCollectionDTO.getBountyHunterFlag() ? flagCount + 1 : flagCount;
        flagCount = developerCollectionDTO.getCampusExpertFlag() ? flagCount + 1 : flagCount;
        calculateDeveloperBO.setFlagCount(flagCount);
        calculateDeveloperBO.setReposCount(developerCollectionDTO.getPublicReposCount());
        calculateDeveloperBO.setGistCount(developerCollectionDTO.getPublicGistsCount());

        totalScore = calculateDeveloperParamContributions(calculateDeveloperBO);
        Set<String> languages = new HashSet<>();
        List<TalentRankProjectPO> projectPOS = new ArrayList<>();
        for (DeveloperProjectCollectionDTO developerProject : developerCollectionDTO.getDeveloperProjectCollectionList()) {
            CalculateRepoBO calculateRepoBO = new CalculateRepoBO();
            //对项目的贡献
            calculateRepoBO.setPullReviewForReposCount(developerProject.getPullRequestReviewEventCount());
            calculateRepoBO.setCommitForReposCount(developerProject.getCommitCount());
            calculateRepoBO.setIssuesForRepsCount(developerProject.getIssuesCommentEventCount());
            //项目影响
            calculateRepoBO.setReposIssuesCount(developerProject.getIssuesCount());
            calculateRepoBO.setReposStarCount(developerProject.getStargazersCount());
            calculateRepoBO.setReposCommitsCount(developerProject.getCommentsCount());
            calculateRepoBO.setReposWatcherCount(developerProject.getWatchersCount());
            BigDecimal projectScore = calculateReposContributions(calculateRepoBO);
            totalScore = projectScore.add(totalScore);
            //添加项目语言
            languages.add(developerProject.getLanguage());

            TalentRankProjectPO talentProjectPO = new TalentRankProjectPO();
            talentProjectPO.setDeveloperId(developerProject.getDeveloperId());
            talentProjectPO.setDescription(developerProject.getDescription());
            talentProjectPO.setUrl(developerProject.getUrl());
            talentProjectPO.setScore(projectScore);
            talentProjectPO.setProjectName(developerProject.getName());
            talentProjectPO.setStarCount(developerProject.getStargazersCount());
           //推测项目分类
            BigModelNew projectCategoryModelNew = new BigModelNew(ctx.toString(), true);
            ProjectCategoryBO projectCategoryBO = new ProjectCategoryBO();
            projectCategoryBO.setDescription(developerProject.getDescription());
            projectCategoryBO.setName(developerProject.getName());
            byte[] categoryBytes = Files.readAllBytes(Paths.get(PROJECT_CATEGORY_PATH));
            String categoryRule = new String(categoryBytes, StandardCharsets.UTF_8);
            CompletableFuture<String> areaFuture = projectCategoryModelNew.requestModel(categoryRule + projectCategoryBO);
            JSONObject areaObj = modelAnswerToJsonObject(areaFuture.get());





            projectPOS.add(talentProjectPO);


        }
        talentRankProjectRepository.saveAll(projectPOS);
        totalScore = totalScore.setScale(2, RoundingMode.HALF_UP);


        //nation大模型推测
        DeveloperNationAnswerBO developerNationAnswerBO = new DeveloperNationAnswerBO();
        developerNationAnswerBO.setName((developerCollectionDTO.getName()));
        developerNationAnswerBO.setLocation(developerCollectionDTO.getLocation());
        developerNationAnswerBO.setBio(developerCollectionDTO.getBlo());
        developerNationAnswerBO.setCompany(developerCollectionDTO.getCompany());
        developerNationAnswerBO.setPronouns(developerCollectionDTO.getPronouns());

        BigModelNew localModelNew = new BigModelNew(ctx.toString(), true);
        byte[] nationBytes = Files.readAllBytes(Paths.get(NATION_FILE_PATH));
        String localRule = new String(nationBytes, StandardCharsets.UTF_8);
        CompletableFuture<String> localFuture = localModelNew.requestModel(localRule + developerNationAnswerBO);
        //等待上次完成
        if (!localFuture.isDone()) {
            Thread.sleep(200);
        }
        String local = localFuture.get();
        JSONObject localObj = modelAnswerToJsonObject(local);
        talentRankPO.setLocationCredence((String) localObj.get("credence"));
        talentRankPO.setLocation((String) localObj.get("nation"));

        //调用大模型获取擅长领域
        BigModelNew areaModelNew = new BigModelNew(ctx.toString(), true);
        DeveloperAreaBO developerAreaBO = new DeveloperAreaBO();
        developerAreaBO.setLanguages(languages);
        developerAreaBO.setBlo(developerCollectionDTO.getBlo());
        byte[] areaBytes = Files.readAllBytes(Paths.get(AREA_FILE_PATH));
        String areaRule = new String(areaBytes, StandardCharsets.UTF_8);
        CompletableFuture<String> areaFuture = areaModelNew.requestModel(areaRule + developerAreaBO);
        JSONObject areaObj = modelAnswerToJsonObject(areaFuture.get());
        talentRankPO.setAreas((String) areaObj.get("area"));
        talentRankPO.setAreaCredence((String) areaObj.get("credence"));

        talentRankPO.setCompany(developerCollectionDTO.getCompany());
        talentRankPO.setName(developerCollectionDTO.getName());
        talentRankPO.setLocation(developerCollectionDTO.getLocation());
        talentRankPO.setLogin(developerCollectionDTO.getLogin());
        talentRankPO.setBlo(developerCollectionDTO.getBlo());
        talentRankPO.setAvatarUrl(developerCollectionDTO.getAvatarUrl());
        talentRankPO.setTalentRank(totalScore);
        talentRankPO.setGitId(developerCollectionDTO.getGitId());
        talentRankRepository.save(talentRankPO);

        ScoreHistoryPO scoreHistoryPO = new ScoreHistoryPO();
        scoreHistoryPO.setDeveloperGitId(gitId);
        scoreHistoryPO.setScore(totalScore);
        scoreHistoryRepository.save(scoreHistoryPO);

        //图数据库添加
//        System.out.println(developerGraphRepository.findByDeveloperId(id));
//        DeveloperGraphEntity existingFollower = developerGraphRepository.findByDeveloperId(id);
//        DeveloperGraphEntity outComing;
//        if (ObjectUtils.isEmpty(existingFollower)) {
//            outComing = new DeveloperGraphEntity((String) user.get("id"), (String) user.get("avatarUrl"), login);
//
//        } else {
//            outComing = existingFollower;
//        }
//        outComing.setScore(calculateSum);

//
//        Map followingMap = (Map) user.get("following");
//        ArrayList<LinkedHashMap<String, String>> followingList = (ArrayList) followingMap.get("nodes");
//        List<DeveloperGraphEntity> inComingList = new ArrayList<>();
//        for (LinkedHashMap<String, String> followingEntity : followingList) {
//            String followeeLogin = followingEntity.get("login");
//            String followeeId = followingEntity.get("id");
//            String avatarUrl = followingEntity.get("avatarUrl");
//
//            // 检查是否已经存在该followee
//            DeveloperGraphEntity existingFollowee = developerGraphRepository.findByDeveloperId(followeeId);
//            DeveloperGraphEntity inComing;
//            if (ObjectUtils.isEmpty(existingFollowee)) {
//                inComing = new DeveloperGraphEntity(followeeId, avatarUrl, followeeLogin);
//            } else {
//                inComing = existingFollowee;
//            }
//            inComingList.add(inComing);
//        }
//
//        // 检查是否已经存在该follower
//
//        outComing.setFollowee(inComingList);
//        developerGraphRepository.save(outComing);


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

    public BigDecimal calculateDeveloperParamContributions(CalculateDeveloperBO entity) {
        // 使用对数缩放和标准化
        double followersScore = Math.log(1 + entity.getFollowersCount());
        double reposScore = 0.1 * entity.getReposCount();
        double gistScore = 0.1 * entity.getGistCount();
        double flagScore = 0.1 * entity.getFlagCount();

        // 调整权重
        return BigDecimal.valueOf(0.5 * followersScore + 0.2 * reposScore + 0.2 * gistScore + 0.1 * flagScore);
    }

    public BigDecimal calculateReposContributions(CalculateRepoBO entity) {
        // 使用对数缩放和标准化
        double pullReviewScore = 0.3 * Math.log(1 + entity.getPullReviewForReposCount());
        double commitScore = 0.6 * Math.log(1 + entity.getCommitForReposCount());
        double issuesScore = 0.1 * Math.log(1 + entity.getIssuesForRepsCount());

        BigDecimal developerForReposParam = BigDecimal.valueOf(pullReviewScore + commitScore + issuesScore);

        // 使用对数缩放和标准化
        double reposCommitsScore = 0.1 * Math.log(1 + entity.getReposCommitsCount());
        double reposIssuesScore = 0.2 * Math.log(1 + entity.getReposIssuesCount());
        double reposWatcherScore = 0.3 * Math.log(1 + entity.getReposWatcherCount());
        double reposStarScore = 0.4 * Math.log(1 + entity.getReposStarCount());

        BigDecimal reposParam = BigDecimal.valueOf(reposCommitsScore + reposIssuesScore + reposWatcherScore + reposStarScore);

        return developerForReposParam.multiply(reposParam);
    }

    public List<TalentRankVO> findAll() {
        List<TalentRankVO> result = new ArrayList<>();
        List<TalentRankPO> talentRankPOS = talentRankRepository.findAllByDeletedFalse();
        Map<String, List<ScoreHistoryPO>> groupedByGitId = scoreHistoryRepository.findAllByDeletedFalse().stream().collect(Collectors.groupingBy(ScoreHistoryPO::getDeveloperGitId));
        Map<String, List<TalentRankProjectPO>> groupedByDeveloperId = talentRankProjectRepository.findAllByDeletedFalse().stream().collect(Collectors.groupingBy(TalentRankProjectPO::getDeveloperId));
        talentRankPOS.forEach(
                talentRankPO -> {
                    TalentRankVO talentRankVO = new TalentRankVO();
                    BeanUtils.copyProperties(talentRankPO, talentRankVO);
                    List<TalentRankProjectPO> talentRankProjectPOS = groupedByDeveloperId.getOrDefault(talentRankPO.getId(), new ArrayList<>());
                    List<TalentRankProjectVO> talentRankProjectVOS = new ArrayList<>();
                    talentRankProjectPOS.forEach(
                            talentRankProjectPO -> {
                                TalentRankProjectVO talentRankProjectVO = new TalentRankProjectVO();
                                BeanUtils.copyProperties(talentRankProjectPO, talentRankProjectVO);
                                talentRankProjectVOS.add(talentRankProjectVO);
                            }
                    );
                    List<ScoreHistoryPO> scoreHistoryPOS = groupedByGitId.getOrDefault(talentRankPO.getGitId(), new ArrayList<>());
                    List<BigDecimal> scoreHistoryList = new ArrayList<>();
                    scoreHistoryPOS.forEach(
                            scoreHistoryPO -> {
                                ScoreHistoryVO scoreHistoryVO = new ScoreHistoryVO();
                                scoreHistoryList.add(scoreHistoryVO.getScore());

                            }

                    );
                    talentRankVO.setScoreHistory(scoreHistoryList);
                    talentRankVO.setProjectList(talentRankProjectVOS);
                    result.add(talentRankVO);

                }
        );
        return  result;
    }

    public TalentRankVO findByLogin(String login) {
        TalentRankVO result = new TalentRankVO();
        TalentRankPO talentRankPO = talentRankRepository.findByLoginAndDeletedFalse(login).orElseThrow(()->new RuntimeException("找不到对应login"));
        BeanUtils.copyProperties(talentRankPO, result);
        List<TalentRankProjectPO> talentRankProjectPOS = talentRankProjectRepository.findAllByDeveloperIdAndDeletedFalse(talentRankPO.getId());
        List<TalentRankProjectVO> talentRankProjectVOS = new ArrayList<>();
        talentRankProjectPOS.forEach(
                talentRankProjectPO -> {
                   TalentRankProjectVO talentRankProjectVO = new TalentRankProjectVO();
                   BeanUtils.copyProperties(talentRankProjectPO, talentRankProjectVO);
                   talentRankProjectVOS.add(talentRankProjectVO);
                }
        );
        List<ScoreHistoryPO> scoreHistoryPOS = scoreHistoryRepository.findAllByDeveloperGitIdAndDeletedFalse(talentRankPO.getGitId());
        List<BigDecimal> scoreHistoryList = new ArrayList<>();
        scoreHistoryPOS.forEach(
                scoreHistoryPO -> {
                    scoreHistoryList.add(scoreHistoryPO.getScore());
                }
        );
        result.setScoreHistory(scoreHistoryList);
        result.setProjectList(talentRankProjectVOS);
        return result;
    }

    /**
     * {
     *
     *         Set<String> languages = new HashSet<>();
     *         BigDecimal calculateSum = BigDecimal.ZERO;
     *
     *         ResponseModel<?> responseModel = calculateClientFeign.searchAllDeveloper(login);
     *         if (!responseModel.isSuccess()) {
     *             throw new RuntimeException(responseModel.getMessageInfo());
     *         }
     *         Map result = (Map) responseModel.getData();
     *         Map data = (Map) result.get("data");
     *
     *         Map user = (Map) data.get("user");
     *         if (ObjectUtils.isEmpty(data.get("user"))) {
     *             return null;
     * //            throw new RuntimeException("无此用户信息" + result.get("errors"));
     *         }
     *         ;
     *         TalentRankTrans talentRankTrans = new TalentRankTrans();
     *         talentRankTrans.setBlo((String) user.get("bio"));
     *         talentRankTrans.setAvatarUrl((String) user.get("avatarUrl"));
     *         String id = (String) user.get("id");
     *         talentRankTrans.setGitId(id);
     *         talentRankTrans.setName((String) user.get("name"));
     *         talentRankTrans.setLogin(login);
     *         List<RepositoryTrans> repositoryTransList = new ArrayList<>();
     *
     *         CalculateEntity calculateEntity = new CalculateEntity();
     *         Integer flagCount = 0;
     *         flagCount = (Boolean) user.get("isDeveloperProgramMember") ? flagCount + 1 : flagCount;
     *         flagCount = (Boolean) user.get("isBountyHunter") ? flagCount + 1 : flagCount;
     *         flagCount = (Boolean) user.get("isCampusExpert") ? flagCount + 1 : flagCount;
     *         Map followerMap = (Map) user.get("followers");
     *
     *
     *         Map gistMap = (Map) user.get("gists");
     *         LinkedHashMap repositoriesList = (LinkedHashMap) user.get("repositories");
     *         ArrayList<LinkedHashMap> nodes = (ArrayList) repositoriesList.get("nodes");
     *         for (LinkedHashMap rep : nodes) {
     *             RepositoryTrans repositoryTrans = new RepositoryTrans();
     *             repositoryTrans.setRepositoryName((String) rep.get("name"));
     *             repositoryTrans.setUrl((String) rep.get("url"));
     *             repositoryTrans.setStarCount((Integer) rep.get("stargazerCount"));
     *             repositoryTransList.add(repositoryTrans);
     *         }
     *         talentRankTrans.setRepositoryTrans(repositoryTransList);
     *
     *         calculateEntity.setGistCount((Integer) gistMap.get("totalCount")).setFlagCount(flagCount).setReposCount((Integer) repositoriesList.get("totalCount"))
     *                 .setFollowersCount((Integer) followerMap.get("totalCount"));
     *
     *
     *         //开发者贡献度先算
     *         calculateSum = calculateDeveloperParamContributions(calculateEntity).add(calculateSum);
     *
     *         LinkedHashMap contributionsCollectionMap = (LinkedHashMap) user.get("contributionsCollection");
     *         ArrayList<LinkedHashMap> pullRequestReviewContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("pullRequestReviewContributionsByRepository");
     *         ArrayList<LinkedHashMap> issueContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("issueContributionsByRepository");
     *         ArrayList<LinkedHashMap> commitContributionsByRepositoryList = (ArrayList<LinkedHashMap>) contributionsCollectionMap.get("commitContributionsByRepository");
     *         for (LinkedHashMap pullRep : pullRequestReviewContributionsByRepositoryList) {
     *             LinkedHashMap rep = (LinkedHashMap) pullRep.get("repository");
     *             if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
     *                 calculateEntity.setReposCommitsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
     *             }
     *             if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
     *                 calculateEntity.setReposWatcherCounts((Integer) ((Map) rep.get("watchers")).get("totalCount"));
     *             }
     *             if (!ObjectUtils.isEmpty(rep.get("issues"))) {
     *                 calculateEntity.setReposStarCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
     *             }
     *             if (!ObjectUtils.isEmpty(pullRep.get("contributions"))) {
     *                 calculateEntity.setPullReviewForReposCount((Integer) ((Map) pullRep.get("contributions")).get("totalCount"));
     *             }
     *             calculateEntity.setReposStarCount((Integer) rep.get("stargazerCount"));
     *             calculateSum = calculateReposContributions(calculateEntity).add(calculateSum);
     *             if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
     *                 languages.add((String) ((Map) rep.get("primaryLanguage")).get("name"));
     *             }
     *
     *
     *         }
     *
     *         for (LinkedHashMap issueRep : issueContributionsByRepositoryList) {
     *             LinkedHashMap rep = (LinkedHashMap) issueRep.get("repository");
     *             if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
     *                 calculateEntity.setReposCommitsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
     *             }
     *             if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
     *                 calculateEntity.setReposWatcherCounts((Integer) ((Map) rep.get("watchers")).get("totalCount"));
     *             }
     *             if (!ObjectUtils.isEmpty(rep.get("issues"))) {
     *                 calculateEntity.setReposStarCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
     *             }
     *             if (!ObjectUtils.isEmpty(issueRep.get("contributions"))) {
     *                 calculateEntity.setIssuesForRepsCount((Integer) ((Map) issueRep.get("contributions")).get("totalCount"));
     *             }
     *             calculateSum = calculateReposContributions(calculateEntity).add(calculateSum);
     *             if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
     *                 languages.add((String) ((Map) rep.get("primaryLanguage")).get("name"));
     *             }
     *         }
     *
     *         for (LinkedHashMap commitRep : commitContributionsByRepositoryList) {
     *             LinkedHashMap rep = (LinkedHashMap) commitRep.get("repository");
     *             if (!ObjectUtils.isEmpty(rep.get("commitComments"))) {
     *                 calculateEntity.setReposCommitsCount((Integer) ((Map) rep.get("commitComments")).get("totalCount"));
     *             }
     *             if (!ObjectUtils.isEmpty(rep.get("watchers"))) {
     *                 calculateEntity.setReposWatcherCounts((Integer) ((Map) rep.get("watchers")).get("totalCount"));
     *             }
     *             if (!ObjectUtils.isEmpty(rep.get("issues"))) {
     *                 calculateEntity.setReposStarCount((Integer) ((Map) rep.get("issues")).get("totalCount"));
     *             }
     *             if (!ObjectUtils.isEmpty(commitRep.get("contributions"))) {
     *                 calculateEntity.setCommitForReposCount((Integer) ((Map) commitRep.get("contributions")).get("totalCount"));
     *             }
     *             calculateSum = calculateReposContributions(calculateEntity).add(calculateSum);
     *             calculateSum = calculateSum.setScale(2, RoundingMode.HALF_UP);
     *             if (!ObjectUtils.isEmpty(rep.get("primaryLanguage"))) {
     *                 languages.add((String) ((Map) rep.get("primaryLanguage")).get("name"));
     *             }
     *         }
     *         talentRankTrans.setRank(calculateSum);
     *
     *
     *         //nation大模型推测
     *         DeveloperNationAnswer developerNationAnswer = new DeveloperNationAnswer();
     *         developerNationAnswer.setName((String) user.get("name"));
     *         developerNationAnswer.setLocation((String) user.get("location"));
     *         developerNationAnswer.setBio((String) user.get("bio"));
     *         developerNationAnswer.setCompany((String) user.get("company"));
     *         developerNationAnswer.setPronouns((String) user.get("pronouns"));
     *         SecurityContextHolder ctx = new SecurityContextHolder();
     *         BigModelNew localModelNew = new BigModelNew(ctx.toString(), true);
     *         byte[] nationBytes = Files.readAllBytes(Paths.get(NATION_FILE_PATH));
     *         String localRule = new String(nationBytes, StandardCharsets.UTF_8);
     *         CompletableFuture<String> localFuture = localModelNew.requestModel(localRule + developerNationAnswer);
     *         //等待上次完成
     *         if (!localFuture.isDone()) {
     *             Thread.sleep(200);
     *         }
     *         String local = localFuture.get();
     *         JSONObject localObj = modelAnswerToJsonObject(local);
     *         talentRankTrans.setLocationCredence((String) localObj.get("credence"));
     *         talentRankTrans.setLocation((String) localObj.get("nation"));
     *
     *         //调用大模型获取擅长领域
     *         BigModelNew areaModelNew = new BigModelNew(ctx.toString(), true);
     *         DeveloperAreaAnswer developerAreaAnswer = new DeveloperAreaAnswer();
     *         System.out.println("language:" + languages);
     *         developerAreaAnswer.setLanguages(languages);
     *         developerAreaAnswer.setBlo((String) user.get("bio"));
     *         byte[] areaBytes = Files.readAllBytes(Paths.get(AREA_FILE_PATH));
     *         String areaRule = new String(areaBytes, StandardCharsets.UTF_8);
     *         CompletableFuture<String> areaFuture = areaModelNew.requestModel(areaRule + developerAreaAnswer);
     *         JSONObject areaObj = modelAnswerToJsonObject(areaFuture.get());
     *         talentRankTrans.setAreas((String) areaObj.get("area"));
     *         talentRankTrans.setAreaCredence((String) areaObj.get("credence"));
     *
     *
     *
     *         //图数据库添加
     *         System.out.println(developerGraphRepository.findByDeveloperId(id));
     *         DeveloperGraphEntity existingFollower = developerGraphRepository.findByDeveloperId(id);
     *         DeveloperGraphEntity outComing;
     *         if (ObjectUtils.isEmpty(existingFollower)) {
     *             outComing = new DeveloperGraphEntity((String) user.get("id"), (String) user.get("avatarUrl"), login);
     *
     *         } else {
     *             outComing = existingFollower;
     *         }
     *         outComing.setScore(calculateSum);
     *
     *
     *         Map followingMap = (Map) user.get("following");
     *         ArrayList<LinkedHashMap<String, String>> followingList = (ArrayList) followingMap.get("nodes");
     *         List<DeveloperGraphEntity> inComingList = new ArrayList<>();
     *         for (LinkedHashMap<String, String> followingEntity : followingList) {
     *             String followeeLogin = followingEntity.get("login");
     *             String followeeId = followingEntity.get("id");
     *             String avatarUrl = followingEntity.get("avatarUrl");
     *
     *             // 检查是否已经存在该followee
     *             DeveloperGraphEntity existingFollowee = developerGraphRepository.findByDeveloperId(followeeId);
     *             DeveloperGraphEntity inComing;
     *             if (ObjectUtils.isEmpty(existingFollowee)) {
     *                 inComing = new DeveloperGraphEntity(followeeId, avatarUrl, followeeLogin);
     *             } else {
     *                 inComing = existingFollowee;
     *             }
     *             inComingList.add(inComing);
     *         }
     *
     *         // 检查是否已经存在该follower
     *
     *         outComing.setFollowee(inComingList);
     *         developerGraphRepository.save(outComing);
     *
     *
     *
     *
     *
     *
     *         return talentRankTrans;
     *
     *     }
     */

}
