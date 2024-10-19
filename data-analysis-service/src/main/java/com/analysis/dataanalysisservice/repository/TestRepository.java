package com.analysis.dataanalysisservice.repository;

import com.analysis.dataanalysisservice.pojo.entity.TEST;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.util.List;

@Repository
public interface TestRepository extends  CrudRepository<TEST,Long>  {
}
