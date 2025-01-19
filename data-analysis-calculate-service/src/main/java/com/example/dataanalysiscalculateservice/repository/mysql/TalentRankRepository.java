package com.example.dataanalysiscalculateservice.repository.mysql;


import com.example.dataanalysiscalculateservice.pojo.po.mysql.TalentRankPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalentRankRepository  extends CrudRepository<TalentRankPO,String> {
    Optional<TalentRankPO> findByGitIdAndDeletedFalse(String gitId);
    List<TalentRankPO>  findAllByOrderByTalentRankDesc();
    Optional<TalentRankPO> findByLoginAndDeletedFalse(String login);
    List<TalentRankPO> findAllByDeletedFalse();
}
