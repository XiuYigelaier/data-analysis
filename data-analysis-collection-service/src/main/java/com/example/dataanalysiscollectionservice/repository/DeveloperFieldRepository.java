package com.example.dataanalysiscollectionservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperFieldRepository  extends CrudRepository<DeveloperFieldRepository,Long> {
}