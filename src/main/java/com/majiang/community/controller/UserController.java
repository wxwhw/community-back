package com.majiang.community.controller;

import cn.hutool.core.lang.UUID;
import com.majiang.community.base.ResultInfo;
import com.majiang.community.exceptions.ParamsException;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.pojo.PwdForm;
import com.majiang.community.pojo.User;
import com.majiang.community.pojo.UserToken;
import com.majiang.community.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author wxh
 * 2023/3/4 13:20
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    UserMapper userMapper;

    @PostMapping("create")
    @ResponseBody
    public ResultInfo userCreate(String email, String nickName, String Pwd, String repeatPwd) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            userService.createAccount(email, nickName, Pwd, repeatPwd);
            resultInfo.setMsg("注册成功！请登录");

        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("注册失败！");
        }
        return resultInfo;
    }

    @PostMapping("login")
    @ResponseBody
    public ResultInfo login(String email, String pwd, HttpServletResponse response) {

        ResultInfo resultInfo = new ResultInfo();
        try {
            User user = userService.userLogin(email, pwd);


            /*登录成功，写入token*/
            String token = UUID.randomUUID().toString();

            /*写入token到数据库*/
            UserToken userToken = new UserToken();
            userToken.setToken(token);
            userToken.setUser(user);
            userMapper.insertToken(userToken.getToken(), userToken.getUser().getId());
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);

            /*设置token*/
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("token",token);
            /*判断是否为管理员*/
            boolean isAdmin = user.getRole().equals("User") ? false : true;
            resultMap.put("isAdmin",isAdmin);

            resultInfo.setResult(resultMap);

            resultInfo.setMsg("登录成功！");

        } catch (ParamsException p) {

            p.printStackTrace();
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());

        } catch (Exception e) {

            e.printStackTrace();
            resultInfo.setCode(505);
            resultInfo.setMsg("登录失败哈哈哈！");
        }

        return resultInfo;
    }

    /*查询所有用户*/
    @GetMapping("findAll")
    @ResponseBody
    public ResultInfo findAllUser() {

        ResultInfo resultInfo = new ResultInfo();
        try {
            List<User> userList = userMapper.findAllUser();
            resultInfo.setResult(userList);
            resultInfo.setMsg("查找成功！");

        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(505);
            resultInfo.setMsg("查询失败！");
        }
        return resultInfo;
    }

    /*插入或更新*/
    @PostMapping("save")
    @ResponseBody
    public ResultInfo save(@RequestBody User user) {

        return userService.save(user);
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResultInfo deleteUser(@PathVariable Long id) {

        ResultInfo resultInfo = new ResultInfo();
        try {
            userService.delete(id);
            resultInfo.setMsg("删除成功！");

        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }
        return resultInfo;
    }

    @GetMapping("/page")
    @ResponseBody
    public Map<String, Object> findPage(@RequestParam(defaultValue = "") String searchname,
                                        @RequestParam(defaultValue = "") String email,
                                        @RequestParam(defaultValue = "") String role,
                                        @RequestParam(defaultValue = "") String state,
                                        @RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize) {

        List<User> allUsers = userMapper.selectPage(searchname, email, role, state, (pageNum - 1) * pageSize, pageSize);

        Long userNum = userMapper.selectTotal(searchname, email, role, state);
        Map<String, Object> res = new HashMap<>();

        res.put("total", userNum);
        res.put("allUsers", allUsers);

        return res;
    }

    @GetMapping("/getUserInfo")
    @ResponseBody
    public ResultInfo getUserById(@RequestParam Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/getUserByToken")
    @ResponseBody
    public ResultInfo getUserByToken(@RequestParam String token){
        return userService.getUserByToken(token);
    }


    @GetMapping("{id}")
    @ResponseBody
    public ResultInfo getUserInfoById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }

    @PostMapping("changePwd")
    @ResponseBody
    public ResultInfo changeUserPwd(@RequestBody PwdForm pwdForm){
        return userService.changeUserPwd(pwdForm);
    }

}