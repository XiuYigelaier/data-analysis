package com.example.core.repository.mysql;

import com.example.core.pojo.entity.mysql.TalentRankProjectEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface TalentRankProjectRepository extends CrudRepository<TalentRankProjectEntity,String> {
    Optional<List<TalentRankProjectEntity>> findAllByTalentRankIdAndDeletedFalse(String talentRankId);
    @Modifying
    @Transactional
    void deleteAllByTalentRankId(String talentRankId);
}
