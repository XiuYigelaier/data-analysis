package com.example.dataanalysisapiservice.pojo.vo;

import com.example.core.enums.ProjectClassificationEnum;
import lombok.Data;

public class ProjectClassification_ProjectVO {
    String projectName;
    String url;
    String description;
    ProjectClassificationEnum projectClassification;

    public ProjectClassification_ProjectVO() {
        // 默认构造方法
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectClassificationEnum getProjectClassification() {
        return projectClassification;
    }

    public void setProjectClassification(ProjectClassificationEnum projectClassification) {
        this.projectClassification = projectClassification;
    }
}