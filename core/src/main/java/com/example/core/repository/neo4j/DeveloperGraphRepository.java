package com.example.core.repository.neo4j;

import com.example.core.pojo.entity.neo4j.DeveloperGraphEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperGraphRepository extends Neo4jRepository<DeveloperGraphEntity,String> {
    DeveloperGraphEntity findByDeveloperId(String id);


}
