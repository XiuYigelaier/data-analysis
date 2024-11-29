package com.example.dataanalysiscollectionservice.pojo.entity;

import com.example.core.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class DeveloperField  extends BaseEntity {
    String language;
}
