package com.example.dataanalysissecurity.controller;


import com.example.core.pojo.base.ResponseModel;
import com.example.dataanalysissecurity.pojo.dto.LoginDTO;
import com.example.dataanalysissecurity.pojo.dto.RegisterDTO;
import com.example.dataanalysissecurity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RequestMapping("/auth")
@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseModel<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            return ResponseModel.success("登录成功",userService.login(loginDTO));
        } catch (Exception e) {
            LOG.error("登录失败" + e.toString(), e);
            return ResponseModel.failure(e,"登录失败");

        }

    }
    @PutMapping("/register")
    public ResponseModel<?> register(@RequestBody RegisterDTO registerDTO) {
        try {
            userService.register(registerDTO);
            return ResponseModel.success("注册成功");
        } catch (Exception e) {
            LOG.error("注册失败" + e.toString(), e);
            return ResponseModel.failure(e,"注册失败");

        }

    }




}
