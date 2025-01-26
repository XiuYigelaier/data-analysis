package com.example.dataanalysisapiservice.pojo.vo;

import com.example.core.enums.ProjectClassificationEnum;

import java.util.ArrayList;
import java.util.List;


public class ProjectClassificationVO {
    ProjectClassificationEnum projectClassification;
    List<ProjectClassification_ProjectVO> projects;

    public ProjectClassificationVO() {
        projects = new ArrayList<>();
    }

    public ProjectClassificationEnum getProjectClassification() {
        return projectClassification;
    }

    public void setProjectClassification(ProjectClassificationEnum projectClassification) {
        this.projectClassification = projectClassification;
    }

    public List<ProjectClassification_ProjectVO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectClassification_ProjectVO> projects) {
        this.projects = projects;
    }


}
