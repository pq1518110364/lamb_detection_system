package com.limen2023.lamb.controller;

import com.limen2023.lamb.config.security.exception.CustomerAuthenticationException;
import com.limen2023.lamb.config.security.service.CustomerUserDetailsService;
import com.limen2023.lamb.utils.JwtUtils;
import com.limen2023.lamb.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/sso")
@Slf4j
public class ISSOController {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private CustomerUserDetailsService customerUserDetailsService;

    @GetMapping("/token")
    public Result token(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getQueryString();
        String servletPath = request.getServletPath();
        String url = request.getRequestURI();
        log.info("========================》  url:" + url + " & queryString:" + path+" & servletPath:"+servletPath);
        log.info("========================》request uri: {}",request.getRequestURI());
        log.info("========================》request ContentType: {}",request.getContentType());

        log.info("========================》response status: {}",response.getStatus());
        log.info("========================》response ContentType: {}",response.getContentType());
        //从头部获取token信息
        String token = request.getHeader("token");
        //如果请求头部没有获取到token，则从请求的参数中进行获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        //如果请求参数中也不存在token信息，则抛出异常
        if (ObjectUtils.isEmpty(token)) {
            throw new CustomerAuthenticationException("token不存在");
        }
        //如果存在token，则从token中解析出用户名
        String username = jwtUtils.getUsernameFromToken(token);
        //如果用户名为空，则解析失败
        if (ObjectUtils.isEmpty(username)) {
            throw new CustomerAuthenticationException("token解析失败");
        }
        //获取用户信息
        UserDetails userDetails =
                customerUserDetailsService.loadUserByUsername(username);
        //判断用户信息是否为空
        if (userDetails == null) {
            throw new CustomerAuthenticationException("token验证失败");
        }
        log.info("token: " + userDetails);
        // 打印response
        log.info("========================》response return data: {} \t" + username);
        return Result.ok(username);
    }
}


