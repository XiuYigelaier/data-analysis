package com.example.core.repository.mysql;


import com.example.core.pojo.entity.mysql.DeveloperAndProjectRelationShipEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperAndProjectRelationShipRepository extends CrudRepository<DeveloperAndProjectRelationShipEntity,String> {
    Optional<DeveloperAndProjectRelationShipEntity> findByDeveloperIdAndProjectIdAndDeletedFalse(String developerId, String projectId);
    @Modifying
    @Transactional
    void deleteAllByDeveloperIdAndDeletedFalse(String developerId);
    Optional<List<DeveloperAndProjectRelationShipEntity>> findAllByDeveloperIdAndDeletedFalse(String developerId);

}
