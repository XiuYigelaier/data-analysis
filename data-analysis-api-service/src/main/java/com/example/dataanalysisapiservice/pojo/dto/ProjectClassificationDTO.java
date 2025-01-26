package com.example.dataanalysisapiservice.pojo.dto;
import com.example.core.enums.ProjectClassificationEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


public class ProjectClassificationDTO {
    ProjectClassificationEnum projectClassification;
    List<ProjectClassification_ProjectDTO> projects;

    public ProjectClassificationDTO() {
        projects = new ArrayList<>();
    }

    public ProjectClassificationEnum getProjectClassification() {
        return projectClassification;
    }

    public void setProjectClassification(ProjectClassificationEnum projectClassification) {
        this.projectClassification = projectClassification;
    }

    public List<ProjectClassification_ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectClassification_ProjectDTO> projects) {
        this.projects = projects;
    }
}
