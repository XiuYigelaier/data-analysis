package com.example.dataanalysissecurity.config;

import com.example.core.pojo.base.LoginUser;

import com.example.core.pojo.base.UserEntity;
import com.example.core.repository.mysql.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDetailServiceByUserName implements UserDetailsService {

    final private UserRepository userRepository;


    public UserDetailServiceByUserName(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(RuntimeException::new);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return new LoginUser(userEntity);
    }
}
