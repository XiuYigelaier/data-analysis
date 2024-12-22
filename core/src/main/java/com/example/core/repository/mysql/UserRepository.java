package com.example.core.repository.mysql;


import com.example.core.pojo.entity.mysql.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {
    Optional<UserEntity> findByUserNameAndDeletedFalse(String username);
}
