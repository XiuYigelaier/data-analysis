package com.example.core.repository;



import com.example.core.entity.Developer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperRepository extends CrudRepository<Developer,String > {
   Optional<Developer> findByGitIdAndDeletedFalse(String gitId);
}
