package com.majiang.community.mapper;

import com.majiang.community.dto.ArticleTypeDTO;
import com.majiang.community.pojo.PostType;
import com.majiang.community.pojo.Tag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * Author wxh
 * 2023/4/4 13:43
 */
public interface PostTypeMapper {

    List<PostType> selectPage(String name, String scope, String auditState, Integer i, Integer pageSize);

    public Long selectTotal(String name, String scope, String auditState);

    @Select("select * from t_article_type where name = #{name}")
    PostType selectPostTypeByName(String name);

    int insertPostType(PostType postType);

    @Update("update t_article_type set audit_state = #{newState},update_at = #{updateAt} where id = #{id}")
    int updateAudit(Long id, String newState, Date updateAt);

    @Delete("delete from t_article_type where id = #{id}")
    int deleteById(Long id);

    @Select("select * from t_article_type where is_delete = 0 and audit_state = 'PASS' order by ref_count desc")
    List<PostType> selectAllTypes();

    @Select("select * from t_article_type where is_delete = 0 and audit_state = 'PASS' and id = #{id}")
    ArticleTypeDTO selectPostTypeById(Long id);

    @Select("select * from t_article_type where is_delete = 0 and name like concat('%', #{searchText}, '%')")
    List<ArticleTypeDTO> selectArticleTypeByBlurName(String searchText);
}
