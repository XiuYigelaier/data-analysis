package com.example.core.pojo.entity.neo4j;


import com.example.core.pojo.base.BaseEntity;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

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

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<DeveloperGraphEntity> followee;

    public DeveloperGraphEntity() {
    }

    public DeveloperGraphEntity(String developerId, String avatarUrl, String login) {

        this.developerId = developerId;
        this.avatarUrl = avatarUrl;
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<DeveloperGraphEntity> getFollowee() {
        return followee;
    }

    public void setFollowee(List<DeveloperGraphEntity> followee) {
        this.followee = followee;
    }


}
