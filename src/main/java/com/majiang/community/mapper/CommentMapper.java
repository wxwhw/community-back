package com.majiang.community.mapper;

import com.majiang.community.dto.CommentDTO;
import com.majiang.community.dto.CommentPageDTO;
import com.majiang.community.pojo.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Author wxh
 * 2023/4/30 23:20
 */
public interface CommentMapper {

    List<Comment> selectRootComByArticleId(Long articleId);

    List<Comment> selectSubComByRootId(Long rootId);

    @Select("select count(*) from t_comments where is_delete = 0 and post_id =#{articleId}")
    Long selectTotalByArticleI(Long articleId);

    Integer insertComment(CommentDTO commentDTO);

    Long selectTotal(String nickname, String content, String startWith, String endWith);

    List<CommentPageDTO> selectAllComments(String nickname, String content, String startWith, String endWith, Integer offset, Integer pageSize);

    @Update("update t_comments set is_delete = 1 where id = #{id} or root_id = #{id}")
    Integer deleteRootById(Long id);

    @Update("update t_comments set is_delete = 1 where id = #{id}")
    Integer deleteSubById(Long id);
}
