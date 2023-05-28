package com.majiang.community.controller;

import com.majiang.community.mapper.UserMapper;
import com.majiang.community.pojo.User;
import com.majiang.community.pojo.UserToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author wxh
 * 2023/3/4 14:25
 */
@Controller
@RequestMapping("/")
public class SystemController {

    @Resource
    private UserMapper userMapper;

    @GetMapping
    public String index(HttpServletRequest request) {

        System.out.println("这里是index");
        Cookie[] cookies = request.getCookies();

        /*cookies非空判断*/
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    UserToken usertoken = null;
                    usertoken = userMapper.selectUserByToken(token);
                    if (usertoken != null) {
                        request.getSession().setAttribute("user", usertoken.getUser());
                    }
                    break;
                }
            }
        }

        return "index";
    }
}
