package com.example.core.repository.mysql;



import com.example.core.pojo.entity.mysql.DeveloperEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperRepository extends CrudRepository<DeveloperEntity,String > {
   Optional<DeveloperEntity> findByGitIdAndDeletedFalse(String gitId);
}
