package com.example.dataanalysissecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 处理密码比对的
 */
//@Component
//public class DaoAuthenticationProviderCustom extends DaoAuthenticationProvider {
//
// @Autowired
// public void setUserDetailsService(UserDetailsService userDetailsService) {
//  super.setUserDetailsService(userDetailsService);
//
//  //重写后就没有userdeatialservice注入方法了就调用父类的再租入一次
// }
//
// @Override
// protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
////它源码时要比对密码。但并不是所有的都需要密码，所以重写他并，设为空
// }
//}
