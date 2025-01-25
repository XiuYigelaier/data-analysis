package com.example.dataanalysiscalculateservice.service;

import com.example.dataanalysiscalculateservice.pojo.po.neo4j.DeveloperGraphPO;
import com.example.dataanalysiscalculateservice.repository.mysql.TalentRankRepository;
import com.example.dataanalysiscalculateservice.repository.neo4j.DeveloperGraphRepository;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PageRankCalculateService {

    @Autowired
    DeveloperGraphRepository developerGraphRepository;
    @Autowired
    TalentRankRepository talentRankRepository;



    public void pageRank() {
        Graph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
        List<DeveloperGraphPO> developerGraphPOList = developerGraphRepository.findAll();
        developerGraphPOList.forEach(
                follower -> {
                    graph.addVertex(follower.getDeveloperId());
                    follower.getFollowee().forEach(
                            followee -> {
                                graph.addEdge(follower.getDeveloperId(), followee.getLogin());
                            }
                    );

                }

        );
        PageRank<String, DefaultEdge> pageRank = new PageRank<>(graph, 0.85);
        Map<String, Double> scores = pageRank.getScores();
        developerGraphPOList.forEach(
                developer -> {
                      developer.setScore(BigDecimal.valueOf(scores.getOrDefault(developer.getDeveloperId(), 0.0)));
                }
        );
        developerGraphRepository.saveAll(developerGraphPOList);

    }


}
