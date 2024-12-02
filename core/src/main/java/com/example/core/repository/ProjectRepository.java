package com.example.core.repository;


import com.example.core.entity.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project,String> {
    Optional<Project> findByGitIdAndDeletedFalse(String gitId);
    void deleteAllByIdInAndDeletedFalse(List<String> projectId);
}
