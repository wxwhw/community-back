package com.majiang.community.mapper;

import com.majiang.community.dto.SearchUserDTO;
import com.majiang.community.pojo.User;
import com.majiang.community.pojo.UserToken;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author wxh
 * 2023/2/28 12:21
 */
public interface UserMapper {

    /*创建账号*/
    public int insertUser(User user);

    /*根据邮箱查找用户*/
    public User selectUserByEmail(String email);

    /*根据昵称查找用户*/
    public User selectUserByName(String nickname);

    /*根据token查询用户*/
    public UserToken selectUserByToken(String token);

    /*插入token*/
    public int insertToken(String token, Long userId);

    @Select("select * from t_user where is_delete = 0")
    List<User> findAllUser();

    public int updateUser(User user);

    @Update("update t_user set is_delete = 1 where id = #{id}")
    public int deleteUser(Long id);

    List<User> selectPage(String searchname, String email, String role, String state, Integer i, Integer pageSize);

    //    @Select("select count(id) from t_user")
    Long selectTotal(String searchname, String email, String role, String state);

    public User selectUserById(Long id);

    @Update("update t_user set avatar = #{relativePath} where id = #{id}")
    int updateAvatarById(String relativePath, Long id);

    List<SearchUserDTO> selectSearchUser(String searchText, Integer offset, Integer pageSize);

    @Select("select password from t_user where is_delete = 0 and id = #{userId}")
    String selectPwdByUserId(Long userId);

    @Update("update t_user set password = #{md5} where id = #{userId}")
    Integer updatePwd(String md5, Long userId);
}
