package com.example.dataanalysiscalculateservice.repository.mysql;

import com.example.core.enums.ProjectClassificationEnum;
import com.example.dataanalysiscalculateservice.pojo.po.mysql.TalentRankProjectPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT p.classification, COUNT(p) FROM TalentRankProjectPO p WHERE p.deleted = false AND p.classification IS NOT NULL  GROUP BY p.classification")
    List<Object[]> findProjectCountGroupedByClassification();


    @Query(value = "SELECT * FROM ( " +
            "SELECT *, ROW_NUMBER() OVER (PARTITION BY classification ORDER BY star_count DESC) as rn " +
            "FROM cal_talent_rank_project " +
            "WHERE is_deleted = false " +
            ") as ranked " +
            "WHERE ranked.rn <= 20  AND classification IS NOT NULL", nativeQuery = true)
    List<TalentRankProjectPO> findTop20ByClassificationAndStarCount();
}
