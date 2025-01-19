package com.example.dataanalysiscalculateservice.pojo.po.neo4j;


import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Node("developer")
public class DeveloperGraphPO {

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
    private List<DeveloperGraphPO> followee;

    public DeveloperGraphPO() {
    }

    public DeveloperGraphPO(String developerId, String avatarUrl, String login) {

        this.developerId = developerId;
        this.avatarUrl = avatarUrl;
        this.login = login;
        this.score = BigDecimal.ZERO;

    }





}
