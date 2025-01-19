package com.example.dataanalysiscalculateservice.repository.mysql;

import com.example.dataanalysiscalculateservice.pojo.po.mysql.ScoreHistoryPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreHistoryRepository extends JpaRepository<ScoreHistoryPO,String>, CrudRepository<ScoreHistoryPO,String> {
    List<ScoreHistoryPO> findAllByDeletedFalse();

    List<ScoreHistoryPO> findAllByDeveloperGitIdAndDeletedFalse(String developerGitId);
}
