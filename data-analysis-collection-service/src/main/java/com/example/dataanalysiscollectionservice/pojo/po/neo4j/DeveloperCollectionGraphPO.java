package com.example.dataanalysiscollectionservice.pojo.po.neo4j;


import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Node("developer_collection_graph_node")
public class DeveloperCollectionGraphPO {

    @Id
    @Property
    private String gitId;
    @Property
    private String avatarUrl;
    @Property
    private String login;
    @Property
    private String name;
    @Property
    private BigDecimal score;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<DeveloperCollectionGraphPO> followings;

    private List<DeveloperCollectionGraphPO> followers;

    public DeveloperCollectionGraphPO() {
        followings = new ArrayList<>();
        followers = new ArrayList<>();

    }

    public DeveloperCollectionGraphPO(String gitId, String avatarUrl, String name, String login, BigDecimal score) {
        this.gitId = gitId;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.login = login;
        this.score = score;
        this.followings = new ArrayList<>();
        this.followers = new ArrayList<>();

    }

}
