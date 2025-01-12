package com.example.dataanalysiscalculateservice.repository;

import com.example.dataanalysiscalculateservice.pojo.po.TalentRankProjectPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TalentRankProjectRepository extends JpaRepository<TalentRankProjectPO,String>, CrudRepository<TalentRankProjectPO,String> {
    List<TalentRankProjectPO> findAllByDeveloperIdAndDeletedFalse(String talentRankId);
    @Modifying
    @Transactional
    void deleteAllByDeveloperId(String talentRankId);
    @Modifying
    @Transactional
    void deleteAllByDeveloperIdAndDeletedFalse(String id);

    List<TalentRankProjectPO> findAllByDeletedFalse();
}
