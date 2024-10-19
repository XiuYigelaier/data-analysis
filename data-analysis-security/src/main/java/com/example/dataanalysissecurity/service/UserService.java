package com.example.dataanalysissecurity.service;

import com.example.dataanalysissecurity.pojo.LoginVO;
import com.example.dataanalysissecurity.pojo.dto.LoginDTO;
import com.example.dataanalysissecurity.pojo.dto.RegisterDTO;
import org.springframework.stereotype.Service;

public interface UserService {

    public void register(RegisterDTO registerDTO);

    public LoginVO login(LoginDTO loginDTO) ;
}
