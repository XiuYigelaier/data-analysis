package com.example.core.repository.mysql;



import com.example.core.pojo.base.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {
    Optional<UserEntity> findByUsernameAndDeletedFalse(String username);
}
