package com.example.dataanalysissecurity.service;

import com.example.dataanalysissecurity.pojo.vo.LoginVO;
import com.example.dataanalysissecurity.pojo.vo.UserVO;
import com.example.dataanalysissecurity.pojo.dto.LoginDTO;
import com.example.dataanalysissecurity.pojo.dto.RegisterDTO;

public interface UserService {

    void register(RegisterDTO registerDTO);

    LoginVO login(LoginDTO loginDTO) ;

    UserVO findUserInfo(String token) throws Exception;
}
