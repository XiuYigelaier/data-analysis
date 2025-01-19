package com.example.dataanalysiscalculateservice.service;

import com.alibaba.fastjson.JSONObject;
import com.example.dataanalysiscalculateservice.enums.ProjectClassificationEnum;
import com.example.core.pojo.base.ResponseModel;
import com.example.dataanalysiscalculateservice.repository.neo4j.DeveloperGraphRepository;
import com.example.dataanalysiscalculateservice.config.BigModelNew;
import com.example.dataanalysiscalculateservice.feign.CalculateClientFeign;
import com.example.dataanalysiscalculateservice.pojo.bo.*;
import com.example.core.pojo.dto.DeveloperCollectionTranDTO;
import com.example.core.pojo.dto.DeveloperProjectCollectionTranDTO;
import com.example.dataanalysiscalculateservice.pojo.po.mysql.ScoreHistoryPO;
import com.example.dataanalysiscalculateservice.pojo.po.mysql.TalentRankPO;
import com.example.dataanalysiscalculateservice.pojo.po.mysql.TalentRankProjectPO;
import com.example.dataanalysiscalculateservice.pojo.vo.ScoreHistoryVO;
import com.example.dataanalysiscalculateservice.pojo.vo.TalentRankProjectVO;
import com.example.dataanalysiscalculateservice.pojo.vo.TalentRankVO;
import com.example.dataanalysiscalculateservice.repository.mysql.ScoreHistoryRepository;
import com.example.dataanalysiscalculateservice.repository.mysql.TalentRankProjectRepository;
import com.example.dataanalysiscalculateservice.repository.mysql.TalentRankRepository;
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
        ResponseModel<List<DeveloperCollectionTranDTO>> responseModel = calculateClientFeign.searchAllDeveloper();
        if (!responseModel.isSuccess()) {
            throw new RuntimeException(responseModel.getMessageInfo());
        }
        responseModel.getData().forEach(
                developerCollectionTranDTO -> {
                    try {
                        calculate(developerCollectionTranDTO);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
        );
    }

    public void calculateByLogin(String login) throws Exception {
        ResponseModel<DeveloperCollectionTranDTO> responseModel = calculateClientFeign.findByProjectUser(login);
        if (!responseModel.isSuccess()) {
            throw new RuntimeException(responseModel.getMessageInfo());
        }

        calculate(responseModel.getData());

    }


    public void calculate(DeveloperCollectionTranDTO developerCollectionTranDTO) throws Exception {
        String gitId = developerCollectionTranDTO.getGitId();
        SecurityContextHolder ctx = new SecurityContextHolder();
        TalentRankPO talentRankPO = talentRankRepository.findByGitIdAndDeletedFalse(gitId).orElseGet(TalentRankPO::new);
        if (StringUtils.hasText(talentRankPO.getId())) {
            talentRankProjectRepository.deleteAllByDeveloperIdAndDeletedFalse(talentRankPO.getId());

        }
        BigDecimal totalScore = BigDecimal.ZERO;
        CalculateDeveloperBO calculateDeveloperBO = new CalculateDeveloperBO();
        calculateDeveloperBO.setGistCount(developerCollectionTranDTO.getPublicGistsCount());
        Integer flagCount = 0;
        flagCount = developerCollectionTranDTO.getDeveloperProgramMemberFlag() ? flagCount + 1 : flagCount;
        flagCount = developerCollectionTranDTO.getBountyHunterFlag() ? flagCount + 1 : flagCount;
        flagCount = developerCollectionTranDTO.getCampusExpertFlag() ? flagCount + 1 : flagCount;
        calculateDeveloperBO.setFlagCount(flagCount);
        calculateDeveloperBO.setReposCount(developerCollectionTranDTO.getPublicReposCount());
        calculateDeveloperBO.setGistCount(developerCollectionTranDTO.getPublicGistsCount());

        totalScore = calculateDeveloperParamContributions(calculateDeveloperBO);
        Set<String> languages = new HashSet<>();
        List<TalentRankProjectPO> projectPOS = new ArrayList<>();
        for (DeveloperProjectCollectionTranDTO developerProject : developerCollectionTranDTO.getDeveloperProjectCollectionList()) {
            CalculateRepoBO calculateRepoBO = new CalculateRepoBO();
            //对项目的贡献
            calculateRepoBO.setPullReviewForReposCount(developerProject.getPullRequestReviewEventCount());
            calculateRepoBO.setCommitForReposCount(developerProject.getCommitCount());
            calculateRepoBO.setIssuesForReposCount(developerProject.getIssuesCommentEventCount());
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
            CompletableFuture<String> categoryFuture = projectCategoryModelNew.requestModel(categoryRule + projectCategoryBO);
            JSONObject categoryObj = modelAnswerToJsonObject(categoryFuture.get());
            talentProjectPO.setClassification( ProjectClassificationEnum.fromString(categoryObj.getString("category")));
            projectPOS.add(talentProjectPO);


        }
        talentRankProjectRepository.saveAll(projectPOS);
        totalScore = totalScore.setScale(2, RoundingMode.HALF_UP);


        //nation大模型推测
        DeveloperNationAnswerBO developerNationAnswerBO = new DeveloperNationAnswerBO();
        developerNationAnswerBO.setName((developerCollectionTranDTO.getName()));
        developerNationAnswerBO.setLocation(developerCollectionTranDTO.getLocation());
        developerNationAnswerBO.setBio(developerCollectionTranDTO.getBio());
        developerNationAnswerBO.setCompany(developerCollectionTranDTO.getCompany());
        developerNationAnswerBO.setPronouns(developerCollectionTranDTO.getPronouns());

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
        developerAreaBO.setBio(developerCollectionTranDTO.getBio());
        byte[] areaBytes = Files.readAllBytes(Paths.get(AREA_FILE_PATH));
        String areaRule = new String(areaBytes, StandardCharsets.UTF_8);
        CompletableFuture<String> areaFuture = areaModelNew.requestModel(areaRule + developerAreaBO);
        JSONObject areaObj = modelAnswerToJsonObject(areaFuture.get());
        talentRankPO.setAreas((String) areaObj.get("area"));
        talentRankPO.setAreaCredence((String) areaObj.get("credence"));

        talentRankPO.setCompany(developerCollectionTranDTO.getCompany());
        talentRankPO.setName(developerCollectionTranDTO.getName());
        talentRankPO.setLocation(developerCollectionTranDTO.getLocation());
        talentRankPO.setLogin(developerCollectionTranDTO.getLogin());
        talentRankPO.setBio(developerCollectionTranDTO.getBio());
        talentRankPO.setAvatarUrl(developerCollectionTranDTO.getAvatarUrl());
        talentRankPO.setTalentRank(totalScore);
        talentRankPO.setGitId(developerCollectionTranDTO.getGitId());
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
        double issuesScore = 0.1 * Math.log(1 + entity.getIssuesForReposCount());

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
                                talentRankProjectVO.setClassification(talentRankProjectPO.getClassification().getTerm());
                                talentRankProjectVOS.add(talentRankProjectVO);
                            }
                    );
                    List<ScoreHistoryPO> scoreHistoryPOS = groupedByGitId.getOrDefault(talentRankPO.getGitId(), new ArrayList<>());
                    List<BigDecimal> scoreHistoryList = new ArrayList<>();
                    scoreHistoryPOS.forEach(
                            scoreHistoryPO -> {
                                 scoreHistoryList.add(scoreHistoryPO.getScore());
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
                    talentRankProjectVO.setClassification(talentRankProjectPO.getClassification().getTerm());
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



}
