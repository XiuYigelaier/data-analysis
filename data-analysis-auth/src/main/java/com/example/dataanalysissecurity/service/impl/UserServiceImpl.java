package com.example.dataanalysissecurity.service.impl;

import com.example.core.entity.LoginUser;
import com.example.core.entity.User;
import com.example.core.repository.UserRepository;
import com.example.core.utils.JWTUtils;
import com.example.core.utils.RedisUtil;
import com.example.dataanalysissecurity.pojo.LoginVO;
import com.example.dataanalysissecurity.pojo.dto.LoginDTO;
import com.example.dataanalysissecurity.pojo.dto.RegisterDTO;
import com.example.dataanalysissecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private RedisUtil redisUtil;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RedisUtil redisUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.redisUtil = redisUtil;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        User user = new User();
        String password = registerDTO.getPassword();
        String userName = registerDTO.getUserName();
        user.setPassword(passwordEncoder.encode(password));
        user.setUserName(userName);
        if (userRepository.findByUserNameAndDeletedFalse(userName).isPresent()) {
            throw new RuntimeException("账号不允许相同");
        }
        userRepository.save(user);
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (ObjectUtils.isEmpty(authentication)) {
            throw new RuntimeException("登录失败");
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JWTUtils.createJWT(UUID.randomUUID().toString(), userId, null);
        redisUtil.set("login:"+userId,loginUser,3600000L);
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(jwt);
        return loginVO;

    }
}
