package com.example.dataanalysiscalculateservice.pojo.vo;


import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.math.BigDecimal;
import java.util.List;

@Data
@Node("developer")
public class DeveloperGraphVO{

    @Id
    @Property
    private String developerId;
    @Property
    private String avatarUrl;
    @Property
    private String login;
    @Property
    private BigDecimal score;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<DeveloperGraphVO> followee;

    public DeveloperGraphVO() {
    }

    public DeveloperGraphVO(String developerId, String avatarUrl, String login) {

        this.developerId = developerId;
        this.avatarUrl = avatarUrl;
        this.login = login;
        this.score = BigDecimal.ZERO;

    }





}
