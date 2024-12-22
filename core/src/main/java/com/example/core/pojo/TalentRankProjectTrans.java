package com.example.core.pojo;

import com.example.core.enums.ProjectClassificationEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class TalentRankProjectTrans implements Serializable {
    String name;
    String url;
    Integer starCount;
    String  description;
    ProjectClassificationEnum classification;
}
