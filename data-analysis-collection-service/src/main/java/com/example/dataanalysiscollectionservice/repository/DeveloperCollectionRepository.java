package com.example.dataanalysiscollectionservice.repository;


import com.example.dataanalysiscollectionservice.pojo.po.DeveloperCollectionPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperCollectionRepository extends CrudRepository<DeveloperCollectionPO,String > {
   Optional<DeveloperCollectionPO> findByGitIdAndDeletedFalse(String gitId);
   List<DeveloperCollectionPO> findAllByDeletedFalse();
   Optional<DeveloperCollectionPO> findByLoginAndDeletedFalse(String login);
}
