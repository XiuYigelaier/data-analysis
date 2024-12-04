package com.example.dataanalysisapiservice.repository;

import com.example.core.entity.DeveloperAndProjectRelationShip;
import com.example.dataanalysisapiservice.pojo.entity.TalentRank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalentRankRepository  extends CrudRepository<TalentRank,String> {
    Optional<TalentRank> findByGitIdAndDeletedFalse(String gitId);
    Optional<List<TalentRank>>  findAllByOrderByTalentRankDesc();
}
