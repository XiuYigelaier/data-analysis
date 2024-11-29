package com.example.dataanalysiscollectionservice.repository;


import com.example.dataanalysiscollectionservice.pojo.entity.Developer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends CrudRepository<Developer,Long> {
   Optional<Developer> findByGitIdAndDeletedFalse(String gitId);
}
