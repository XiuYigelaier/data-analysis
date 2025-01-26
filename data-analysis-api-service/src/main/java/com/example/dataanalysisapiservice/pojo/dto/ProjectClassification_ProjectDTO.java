package com.example.dataanalysisapiservice.pojo.dto;

import com.example.core.enums.ProjectClassificationEnum;
import lombok.Data;

import java.io.Serializable;

public class ProjectClassification_ProjectDTO implements Serializable {
    String projectName;
    String url;
    String description;
    ProjectClassificationEnum classification;
    public ProjectClassification_ProjectDTO() {
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

    public ProjectClassificationEnum getClassification() {
        return classification;
    }

    public void setClassification(ProjectClassificationEnum classification) {
        this.classification = classification;
    }
}