package com.majiang.community.mapper;

import com.majiang.community.dto.UserDTO;
import com.majiang.community.pojo.Article;
import com.majiang.community.pojo.UserFollow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Author wxh
 * 2023/5/4 23:07
 */
public interface FollowMapper {

    @Insert("INSERT into t_user_follow(followed,follow_type,user_id,is_delete,create_at,update_at) VALUES(#{followed},#{followType},#{userId},#{isDelete},#{createAt},#{updateAt})")
    Integer insertUserFollow(UserFollow userFollow);

    List<UserDTO> selectUserFollowList(Long userId,Integer offset,Integer pageSize);

    Long selectFollowNum(Long userId,Long followed,String followType);

    List<Article> selectFavArticle(Long userId,Integer offset,Integer pageSize);

    UserFollow selectUserIsFollowed(Long followed,Long userId);

    @Update("update t_user_follow set is_delete = 1 where followed = #{followed} and user_id = #{userId} and follow_type = #{followType}")
    Integer removeUserFollow(Long followed, Long userId, String followType);

    UserFollow selectIsExisted(UserFollow userFollow);

    @Update("update t_user_follow set is_delete = 0 where followed = #{followed} and user_id = #{userId}")
    Integer updateUserFollow(UserFollow userFollow);

    List<UserDTO> selectUserFansList(Long userId, Integer offset, Integer pageSize);
}
