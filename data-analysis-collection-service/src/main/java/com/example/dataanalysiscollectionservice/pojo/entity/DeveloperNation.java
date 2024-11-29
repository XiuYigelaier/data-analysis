package com.example.dataanalysiscollectionservice.pojo.entity;

import com.example.core.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class DeveloperNation  extends BaseEntity {
    private String developerId;
    private String location;
    private String workTime;
    private String NameLanguage;
}
