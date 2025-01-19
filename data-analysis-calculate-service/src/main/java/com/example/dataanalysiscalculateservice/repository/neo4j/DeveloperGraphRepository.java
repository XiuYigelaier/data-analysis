package com.example.dataanalysiscalculateservice.repository.neo4j;

import com.example.dataanalysiscalculateservice.pojo.po.neo4j.DeveloperGraphPO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperGraphRepository extends Neo4jRepository<DeveloperGraphPO,String> {
    DeveloperGraphPO findByDeveloperId(String id);


}
