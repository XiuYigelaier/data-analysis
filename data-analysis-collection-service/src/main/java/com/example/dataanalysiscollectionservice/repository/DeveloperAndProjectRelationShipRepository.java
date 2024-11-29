package com.example.dataanalysiscollectionservice.repository;

import com.example.dataanalysiscollectionservice.pojo.entity.Developer;
import com.example.dataanalysiscollectionservice.pojo.entity.DeveloperAndProjectRelationShip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperAndProjectRelationShipRepository extends CrudRepository<DeveloperAndProjectRelationShip,Long> {
}
