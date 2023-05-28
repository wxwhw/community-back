package com.majiang.community.controller;

import com.alibaba.fastjson2.JSON;
import com.majiang.community.dto.AccessTokenDTO;
import com.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Author wxh
 * 2023/2/8 17:16
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,@RequestParam(name="state") String state){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("7d66ee207fc9e6bc3068");
        accessTokenDTO.setClient_secret("fd7ad7f1a030c549914617518efe5b284e91e8e5");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:9505/callback");

        System.out.println(JSON.toJSONString(accessTokenDTO));
        System.out.println(githubProvider.getAccessToken(accessTokenDTO));

        return "index";
    }
}
