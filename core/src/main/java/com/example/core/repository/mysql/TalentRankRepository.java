package com.example.core.repository.mysql;

import com.example.core.pojo.entity.mysql.TalentRankEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalentRankRepository  extends CrudRepository<TalentRankEntity,String> {
    Optional<TalentRankEntity> findByGitIdAndDeletedFalse(String gitId);
    Optional<List<TalentRankEntity>>  findAllByOrderByTalentRankDesc();

    List<TalentRankEntity> findAllByDeletedIsFalse();
    Optional<TalentRankEntity> findByLoginAndDeletedFalse(String login);
}
