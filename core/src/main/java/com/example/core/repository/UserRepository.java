package com.example.core.repository;



import com.example.core.pojo.base.UserPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserPO,String> {
    Optional<UserPO> findByUsernameAndDeletedFalse(String username);
}
