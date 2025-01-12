package com.example.dataanalysiscollectionservice.repository;


import com.example.dataanalysiscollectionservice.pojo.po.DeveloperProjectCollectionPO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperProjectCollectionRepository extends CrudRepository<DeveloperProjectCollectionPO,String> {
    Optional<DeveloperProjectCollectionPO> findByGitIdAndDeletedFalse(String gitId);
    @Modifying
    @Transactional
    void deleteAllByIdInAndDeletedFalse(List<String> projectId);

    List<DeveloperProjectCollectionPO> findAllByDeletedFalseAndIdIn(List<String> projectId);
}
