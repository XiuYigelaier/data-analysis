package com.example.dataanalysissecurity.service.impl;

import com.example.core.pojo.base.LoginUser;

import com.example.core.pojo.base.UserPO;
import com.example.core.repository.UserRepository;
import com.example.core.utils.JWTUtils;
import com.example.core.utils.RedisUtil;
import com.example.dataanalysissecurity.pojo.vo.LoginVO;
import com.example.dataanalysissecurity.pojo.vo.UserVO;
import com.example.dataanalysissecurity.pojo.dto.LoginDTO;
import com.example.dataanalysissecurity.pojo.dto.RegisterDTO;
import com.example.dataanalysissecurity.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
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
        UserPO userPO = new UserPO();
        String password = registerDTO.getPassword();
        String userName = registerDTO.getUsername();
        userPO.setPassword(passwordEncoder.encode(password));
        userPO.setUsername(userName);
        if (userRepository.findByUsernameAndDeletedFalse(userName).isPresent()) {
            throw new RuntimeException("账号不允许相同");
        }
        userRepository.save(userPO);
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (ObjectUtils.isEmpty(authentication)) {
            throw new RuntimeException("登录失败");
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId();
        String jwt = JWTUtils.createJWT(UUID.randomUUID().toString(), userId, null);
        redisUtil.set("login:" + userId, loginUser, 3600000L);
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(jwt);
        return loginVO;

    }

    @Override
    public UserVO findUserInfo(String token) throws Exception {
        Claims claim = JWTUtils.parseJWT(token);
        String userId = claim.getSubject();
        UserVO userVO = new UserVO();
        userRepository.findById(userId).ifPresent(user -> {
            userVO.setUsername(user.getUsername());
            userVO.setAvatar(user.getAvatar());
        });
        return userVO;
    }
}
