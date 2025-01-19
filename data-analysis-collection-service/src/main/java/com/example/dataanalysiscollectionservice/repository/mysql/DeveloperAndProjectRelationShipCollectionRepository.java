package com.example.dataanalysiscollectionservice.repository.mysql;



import com.example.dataanalysiscollectionservice.pojo.po.mysql.DeveloperAndProjectRelationShipCollectionPO;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperAndProjectRelationShipCollectionRepository extends CrudRepository<DeveloperAndProjectRelationShipCollectionPO,String> {
    Optional<DeveloperAndProjectRelationShipCollectionPO> findByDeveloperIdAndProjectIdAndDeletedFalse(String developerId, String projectId);
    @Modifying
    @Transactional
    void deleteAllByDeveloperIdAndDeletedFalse(String developerId);
    List<DeveloperAndProjectRelationShipCollectionPO> findAllByDeveloperIdAndDeletedFalse(String developerId);

    List<DeveloperAndProjectRelationShipCollectionPO> findAllByDeletedFalseAndDeveloperIdIn(List<String> developerIds);

    List<DeveloperAndProjectRelationShipCollectionPO> findAllByDeletedFalseAndDeveloperId(String id);
}
