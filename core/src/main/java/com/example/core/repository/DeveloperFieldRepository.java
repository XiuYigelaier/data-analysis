package com.example.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperFieldRepository  extends CrudRepository<DeveloperFieldRepository,String> {
}