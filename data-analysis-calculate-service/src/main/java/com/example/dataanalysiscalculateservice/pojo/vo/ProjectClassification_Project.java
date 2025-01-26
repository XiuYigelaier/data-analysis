package com.example.dataanalysiscalculateservice.pojo.vo;

import com.example.core.enums.ProjectClassificationEnum;
import lombok.Data;

@Data
public class ProjectClassification_Project {
    String projectName;
    String url;
    String description;
    ProjectClassificationEnum classification;


}