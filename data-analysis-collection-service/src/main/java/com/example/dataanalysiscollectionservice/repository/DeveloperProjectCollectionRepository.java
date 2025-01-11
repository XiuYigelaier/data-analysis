package com.example.core.repository.mysql;


import com.example.core.pojo.entity.mysql.DeveloperProjectEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<DeveloperProjectEntity,String> {
    Optional<DeveloperProjectEntity> findByGitIdAndDeletedFalse(String gitId);
    @Modifying
    @Transactional
    void deleteAllByIdInAndDeletedFalse(List<String> projectId);
}
