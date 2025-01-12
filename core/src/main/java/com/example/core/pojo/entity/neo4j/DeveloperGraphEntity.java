package com.example.core.pojo.entity.neo4j;


import com.example.core.pojo.base.BaseEntity;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Node("developer")
public class DeveloperGraphEntity  {

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
    private List<DeveloperGraphEntity> followee;

    public DeveloperGraphEntity() {
    }

    public DeveloperGraphEntity(String developerId, String avatarUrl, String login) {

        this.developerId = developerId;
        this.avatarUrl = avatarUrl;
        this.login = login;
        this.score = BigDecimal.ZERO;

    }





}
