package com.example.dataanalysiscollectionservice.repository.neo4j;

import com.example.dataanalysiscollectionservice.pojo.po.neo4j.DeveloperCollectionGraphPO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeveloperGraphRepository extends Neo4jRepository<DeveloperCollectionGraphPO,String> {
    @Query("MATCH (n:developer_collection_graph_node) OPTIONAL MATCH (n)-[r:FOLLOWS]->(m) RETURN n, collect(r), collect(m)")
    List<DeveloperCollectionGraphPO> findAllNodesWithRelationships();


}
