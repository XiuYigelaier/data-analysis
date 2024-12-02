package com.example.core.repository;


import com.example.core.entity.DeveloperAndProjectRelationShip;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperAndProjectRelationShipRepository extends CrudRepository<DeveloperAndProjectRelationShip,String> {
    Optional<DeveloperAndProjectRelationShip> findByDeveloperIdAndProjectIdAndDeletedFalse(String developerId,String projectId);
    @Modifying
    @Transactional
    void deleteAllByDeveloperIdAndDeletedFalse(String developerId);
    Optional<List<DeveloperAndProjectRelationShip>> findAllByDeveloperIdAndDeletedFalse(String developerId);

}
