package com.analysis.dataanalysisservice.repository;

import com.analysis.dataanalysisservice.pojo.entity.Developer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperRepository extends CrudRepository<Developer,Long> {
    Optional<Developer> findByNameAndDeletedFalse(String name);
}
