package com.majiang.community.mapper;

import com.majiang.community.dto.ArticleDTO;
import com.majiang.community.dto.ArticleTypeDTO;
import com.majiang.community.dto.RelatedArticleDTO;
import com.majiang.community.dto.UserDTO;
import com.majiang.community.pojo.Article;
import com.majiang.community.pojo.UserFollow;
import com.majiang.community.pojo.UserLike;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * Author wxh
 * 2023/4/28 16:13
 */
public interface ArticleMapper {

    public int insertArticle(Article article);

    public int insertTagInArticle(Article article);

    Long selectTotal(Long typeId, String auditState, Byte official, Byte top, Byte marrow, Long authorId, String title);

    List<ArticleDTO> selectPage(Long typeId, String auditState, Byte official, Byte top, Byte marrow, Long authorId, String title, Integer i, Integer pageSize);

    @Select("select id,nickname,avatar from t_user where is_delete = 0 and id = #{id}")
    UserDTO selectUserById(Long id);

    @Select("select id,name,description,scope,ref_count from t_article_type where is_delete = 0 and id = #{id}")
    ArticleTypeDTO selectArticleTypeById(Long typeId);


    ArticleDTO selectArticleById(Long id);

    @Update("update t_posts set views = views + 1 where id = #{id}")
    void updateViews(Long id);

    @Update("update t_posts set approvals = approvals + 1 where id = #{id}")
    int updateApprovals(Long id);

    @Update("update t_posts set audit_state = #{auditState},marrow = #{marrow},official=#{official},top=#{top},update_at= #{updateAt} where id = #{id}")
    int updateAudit(Long id, String auditState, Byte marrow, Byte official, Byte top, Date updateAt);

    @Update("update t_posts set is_delete = 1 where id = #{id}")
    int deleteById(Long id);

    List<RelatedArticleDTO> selectRelatedArticleList(Long articleId);

    List<ArticleDTO> selectSearchArticle(String searchText,Integer offset,Integer pageSize);

    @Insert("insert into t_user_like(article_id,user_id,is_delete,create_at,update_at) value(#{articleId},#{userId},#{isDelete},#{createAt},#{updateAt})")
    Integer insertApproval(UserLike userLike);

    @Update("update t_posts set approvals = approvals + 1 where id = #{articleId}")
    Integer updateApprovalNum(Long articleId);

    @Select("select * from t_user_like where article_id = #{articleId} and user_id = #{userId}")
    UserLike selectUserLiked(Long articleId, Long userId);

    @Delete("delete from t_user_like where article_id = #{articleId} and user_id = #{userId}")
    Integer deleteUserLike(UserLike userLike);

    @Update("update t_posts set approvals = approvals - 1 where id = #{articleId}")
    Integer deleteApproval(Long articleId);

    @Update("update t_posts set comments = comments + 1 where id = #{postId}")
    Integer updateCommentNum(Long postId);

    @Update("update t_posts set comments = comments - 1 where id = #{postId}")
    Integer deleteCommentNum(Long postId);

    @Select("select count(*) from t_posts where id in (select article_id from t_tag_posts_mapping where is_delete = 0 and tag_id = #{tagId}) and audit_state = 'PASS' and is_delete = 0")
    Long selectTotalByTagId(Long tagId);

    List<ArticleDTO> selectArticleByTagId(Long tagId, Integer offset, Integer pageSize);

    @Select("select * from t_user_follow where followed = #{followed} and user_id = #{userId} and follow_type = 'Article'")
    UserFollow selectUserColloected(Long followed, Long userId);

    @Insert("insert into t_user_follow(followed,follow_type,user_id,is_delete,create_at,update_at) value(#{followed},'Article',#{userId},#{isDelete},#{createAt},#{updateAt})")
    Integer insertCollect(UserFollow userFollow);

    @Update("update t_posts set collections = collections + 1 where id = #{followed}")
    Integer updateCollections(Long followed);

    @Delete("delete from t_user_follow where followed = #{followed} and user_id = #{userId} and follow_type = 'Article'")
    Integer deleteUserFollowed(UserFollow userFollow);

    @Update("update t_posts set collections = collections - 1 where id = #{followed}")
    Integer deleteCollections(Long followed);

    @Select("select count(*) from t_posts where id in (select followed from t_user_follow where user_id = #{userId} and is_delete = 0 and follow_type = 'Article' ) and is_delete = 0 and audit_state = 'PASS'")
    Long selectTotalColArticle(Long userId);

    List<ArticleDTO> selectColArticle(Long userId, Integer offset, Integer pageSize);
}
