package com.example.dataanalysiscollectionservice.pojo.vo.neo4j;

import com.example.dataanalysiscollectionservice.pojo.po.neo4j.DeveloperCollectionGraphPO;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DeveloperCollectionGraphVO {


    private String gitId;

    private String avatarUrl;

    private String login;

    private String name;

    private BigDecimal score;
    private List<DeveloperCollectionGraphPO> followings;

    private List<DeveloperCollectionGraphPO> followers;
}
