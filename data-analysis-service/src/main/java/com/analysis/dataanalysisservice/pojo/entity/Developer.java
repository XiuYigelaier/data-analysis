package com.analysis.dataanalysisservice.pojo.entity;

import com.example.core.entity.BaseEntity;
import lombok.Data;
import org.apache.tomcat.websocket.BackgroundProcess;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class Developer  extends BaseEntity {
    @Column(name = "name")
    String name;
    @Column(name = "local")
    String local;
    @Column(name = "project")
    String project;
    @Column(name = "issuesNum")
    Long issuesNum;
    @Column(name = "commitsNum")
    Long commitsNum;
    @Column(name = "actionsNum")
    Long actionsNum;
    @Column(name = "contribute")
    Integer contribute;


}
