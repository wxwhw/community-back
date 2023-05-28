package com.majiang.community.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Author wxh
 * 2023/2/28 11:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Base{

    /*邮箱*/
    private String email;

    /*昵称*/
    private String nickname;

    /*密码*/
    @JsonIgnore
    private String password;

    /*角色*/
    private String role;

    /*状态*/
    private String state;

    /*性别*/
    private String gender;

    /*头像*/
    private String avatar;

    /*个性签名*/
    private String signature;

    /*是否删除*/
    private Byte isDelete;

    /*最后登录时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

}
