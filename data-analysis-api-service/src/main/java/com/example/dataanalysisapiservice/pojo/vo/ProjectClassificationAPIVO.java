package com.example.dataanalysisapiservice.pojo.vo;

import com.example.core.enums.ProjectClassificationEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectClassificationAPIVO {
    ProjectClassificationEnum projectClassification;
    List<ProjectClassification_Project> projects;
    Long count;

    public ProjectClassificationAPIVO() {
        projects = new ArrayList<>();
    }

    @Data
    public class ProjectClassification_Project {
        String projectName;
        String url;
        String description;
        ProjectClassificationEnum projectClassification;


    }
}
