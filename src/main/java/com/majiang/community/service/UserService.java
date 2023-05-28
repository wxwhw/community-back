package com.majiang.community.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.majiang.community.base.ResultInfo;
import com.majiang.community.exceptions.ParamsException;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.pojo.PwdForm;
import com.majiang.community.pojo.User;
import com.majiang.community.utils.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Author wxh
 * 2023/2/28 12:38
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User userLogin(String email, String pwd) {

        /*参数非空判断*/
        checkLoginParams(email, pwd);

        /*查询用户是否存在*/
        User user = userMapper.selectUserByEmail(email);
        AssertUtil.isTrue(user == null, "用户不存在！");

        /*判断密码是否正确*/
        pwd = SecureUtil.md5(pwd);
        AssertUtil.isTrue(!pwd.equals(user.getPassword()), "密码不正确！");

        /*判断用户是非删除*/
        AssertUtil.isTrue(user.getIsDelete() != 0,"用户已注销！");

        /*判断用户是否禁用*/
        AssertUtil.isTrue(user.getState().equals("Unable"),"您已被禁用！");

        /*更新用户登录时间*/
        user.setLastLoginTime(new Date());

        userMapper.updateUser(user);
        /*返回用户对象*/
        return user;
    }


    public void createAccount(String email, String nickName, String Pwd, String repeatPwd) {

        /*参数校验*/
        checkCreateParams(email, nickName, Pwd, repeatPwd);

        User user = new User();

        /*MD5加密*/
        user.setPassword(SecureUtil.md5(Pwd));

        /*初始化账号信息*/
        user.setEmail(email);
        user.setNickname(nickName);
        user.setRole("User");
        user.setState("Enable");
        user.setIsDelete((byte) 0);
        user.setCreateAt(new Date());
        user.setUpdateAt(new Date());
        user.setGender("男");
        user.setAvatar("image/default-face.webp");

        /*执行插入，返回受影响的行数*/
        AssertUtil.isTrue(userMapper.insertUser(user) < 1, "注册失败！无法插入数据库");

    }

    public void checkCreateParams(String email, String nickname, String Pwd, String repeatPwd) {

        AssertUtil.isTrue(StrUtil.isBlank(email), "注册邮箱不能为空！");
        User user = null;
        user = userMapper.selectUserByEmail(email);
        AssertUtil.isTrue(user != null, "邮箱已存在！");

        AssertUtil.isTrue(StrUtil.isBlank(nickname), "昵称不能为空！");
        user = userMapper.selectUserByName(nickname);
        AssertUtil.isTrue(user != null, "昵称已存在！");

        AssertUtil.isTrue(StrUtil.isBlank(Pwd), "密码不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(repeatPwd), "确认密码不能为空！");
        AssertUtil.isTrue(!Pwd.equals(repeatPwd), "输入密码与确认密码不一致！");
    }

    public void checkLoginParams(String email, String pwd) {

        AssertUtil.isTrue(StrUtil.isBlank(email), "登录邮箱不能为空！");

        AssertUtil.isTrue(StrUtil.isBlank(pwd), "密码不能为空");

    }


    public ResultInfo updateUser(User user) {

        ResultInfo resultInfo = new ResultInfo();
        /*更新时间*/
        user.setUpdateAt(new Date());

        try {
            /*参数校验*/
            checkUpdateParams(user);
            AssertUtil.isTrue(userMapper.updateUser(user) < 1, "修改失败！请重试");
            resultInfo.setMsg("保存成功！");
        } catch (ParamsException p) {
            resultInfo.setMsg(p.getMsg());
            resultInfo.setCode(505);

        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }

        return resultInfo;

    }

    public ResultInfo insertUser(User user) {

        ResultInfo resultInfo = new ResultInfo();
        /*MD5加密*/
        user.setPassword(SecureUtil.md5(user.getPassword()));
        /*初始化账号信息*/
        user.setIsDelete((byte) 0);
        user.setCreateAt(new Date());
        user.setUpdateAt(new Date());

        try {
            checkInsertParams(user);
            AssertUtil.isTrue(userMapper.insertUser(user) < 1, "注册失败！无法插入数据库");
        } catch (ParamsException p) {
            resultInfo.setMsg(p.getMsg());
            resultInfo.setCode(505);
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }

        return resultInfo;
    }

    public void checkInsertParams(User user) {

        AssertUtil.isTrue(StrUtil.isBlank(user.getEmail()), "注册邮箱不能为空！");

        User insertUser = null;
        insertUser = userMapper.selectUserByEmail(user.getEmail());
        AssertUtil.isTrue(insertUser != null, "邮箱已存在！");

        AssertUtil.isTrue(StrUtil.isBlank(user.getNickname()), "昵称不能为空！");
        insertUser = userMapper.selectUserByName(user.getNickname());
        AssertUtil.isTrue(insertUser != null, "昵称已存在！");
        AssertUtil.isTrue(StrUtil.isBlank(user.getPassword()), "密码不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(user.getRole()), "角色不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(user.getGender()), "性别不能为空！");

    }

    public void checkUpdateParams(User user) {

        User oldUserInfo = userMapper.selectUserById(user.getId());

        if (!user.getNickname().equals(oldUserInfo.getNickname())) {

            AssertUtil.isTrue(StrUtil.isBlank(user.getNickname()), "昵称不能为空！");
            AssertUtil.isTrue(userMapper.selectUserByName(user.getNickname()) != null, "昵称已存在！");

            if (!user.getEmail().equals(oldUserInfo.getEmail())) {
                AssertUtil.isTrue(StrUtil.isBlank(user.getEmail()), "注册邮箱不能为空！");
                AssertUtil.isTrue(userMapper.selectUserByEmail(user.getEmail()) != null, "邮箱已存在！");
            }

            AssertUtil.isTrue(StrUtil.isBlank(user.getRole()), "角色不能为空！");
            AssertUtil.isTrue(StrUtil.isBlank(user.getGender()), "性别不能为空！");
        }
    }

    public ResultInfo save(User user) {

        if (user.getId() == null) {//新增
            return insertUser(user);
        } else { //更新
            return updateUser(user);
        }
    }

    public void delete(Long id) {

        AssertUtil.isTrue(userMapper.deleteUser(id) < 1, "删除失败！");
    }


    public ResultInfo getUserById(Long id) {

        ResultInfo resultInfo = new ResultInfo();
        try {
            User user = userMapper.selectUserById(id);
            AssertUtil.isTrue(user == null, "用户不存在！");
            resultInfo.setMsg("用户查找成功！");
            resultInfo.setResult(user);
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

    public ResultInfo getUserByToken(String token) {
        ResultInfo resultInfo = new ResultInfo();

        try{
            User user = userMapper.selectUserByToken(token).getUser();
            AssertUtil.isTrue(user == null,"用户不存在！");
            resultInfo.setResult(user);
            resultInfo.setMsg("获取用户信息成功！");
        } catch (ParamsException p){
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("获取用户信息失败！");
        }
        return resultInfo;
    }

    public ResultInfo uploadAvatarById(String relativePath, Long id) {

        ResultInfo resultInfo = new ResultInfo();
        try{
            AssertUtil.isTrue(userMapper.updateAvatarById(relativePath,id) < 1,"更新头像到数据库失败！");
            resultInfo.setCode(200);
            resultInfo.setMsg("更新头像成功！");
        }  catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("更新头像失败！");
        }
        return resultInfo;
    }

    public ResultInfo changeUserPwd(PwdForm pwdForm) {
        ResultInfo resultInfo = new ResultInfo();
        try{
            checkPwd(pwdForm);
            AssertUtil.isTrue(userMapper.updatePwd(SecureUtil.md5(pwdForm.getNewPwd()),pwdForm.getUserId()) < 1,"更新密码失败！");
            resultInfo.setMsg("更改成功！");
        } catch (ParamsException p){
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");
        }
        return resultInfo;
    }

    public void checkPwd(PwdForm pwdForm){
        AssertUtil.isTrue(pwdForm.getUserId() == null,"获取用户Id失败！");
        AssertUtil.isTrue(StrUtil.isBlank(pwdForm.getOldPwd()) || StrUtil.isBlank(pwdForm.getNewPwd()) || StrUtil.isBlank(pwdForm.getRepeatPwd()),"输入密码不能为空！");
        String truePwd = userMapper.selectPwdByUserId(pwdForm.getUserId());
        AssertUtil.isTrue(!SecureUtil.md5(pwdForm.getOldPwd()).equals(truePwd),"旧密码不正确");
        AssertUtil.isTrue(!pwdForm.getNewPwd().equals(pwdForm.getRepeatPwd()),"两次密码不一致");
        AssertUtil.isTrue(truePwd.equals(SecureUtil.md5(pwdForm.getNewPwd())),"请输入与旧密码不同的密码");
    }
}
