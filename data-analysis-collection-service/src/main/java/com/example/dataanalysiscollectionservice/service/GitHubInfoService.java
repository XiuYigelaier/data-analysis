package com.example.dataanalysiscollectionservice.service;

import java.util.List;

public interface GitHubInfoService {

    public List<String> getRepos(String projectUser);

    Object calculateContribution(String projectUser, String repos);
    public void  graphql();
}
