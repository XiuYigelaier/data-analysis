package com.example.dataanalysiscollectionservice.repository;

import com.example.dataanalysiscollectionservice.pojo.entity.Developer;
import com.example.dataanalysiscollectionservice.pojo.entity.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project,Long> {
}
