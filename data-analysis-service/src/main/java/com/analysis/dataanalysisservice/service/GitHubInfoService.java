package com.analysis.dataanalysisservice.service;

import org.springframework.stereotype.Service;

import java.util.List;

public interface GitHubInfoService {

    public List<String> getRepos(String projectUser);

    Object calculateContribution(String projectUser, String repos);
}
