package com.example.dataanalysissecurity.repository;

import com.example.dataanalysissecurity.pojo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByUserNameAndDeletedFalse(String username);
}
