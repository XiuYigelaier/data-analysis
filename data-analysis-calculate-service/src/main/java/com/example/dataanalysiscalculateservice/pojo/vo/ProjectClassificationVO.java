package com.example.dataanalysiscalculateservice.pojo.vo;

import com.example.core.enums.ProjectClassificationEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectClassificationVO {
    ProjectClassificationEnum projectClassification;
    List<ProjectClassification_Project> projects;

    public ProjectClassificationVO() {
        projects = new ArrayList<>();
    }

    @Data
    public class ProjectClassification_Project {
        String projectName;
        String url;
        String description;
        ProjectClassificationEnum classification;


    }
}
