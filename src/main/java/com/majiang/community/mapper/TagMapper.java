package com.majiang.community.mapper;

import com.majiang.community.dto.Group;
import com.majiang.community.dto.TagDTO;
import com.majiang.community.pojo.Tag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.thymeleaf.processor.templateboundaries.ITemplateBoundariesProcessor;

import java.util.Date;
import java.util.List;

/**
 * Author wxh
 * 2023/4/3 14:04
 */
public interface TagMapper {

    List<Tag> selectPage(String name, String group, String auditState, Integer i, Integer pageSize);

    Long selectTotal(String name,String group,String auditState);

    @Update("update t_tag set audit_state = #{newState},update_at = #{updateAt} where id = #{id}")
    int updateAudit(Long id, String newState, Date updateAt);

    int insertTag(Tag tag);

    @Select("select * from t_tag where is_delete = 0 and name = #{name}")
    Tag selectTagByName(String name);

    @Select("select * from t_tag where is_delete = 0 and name like concat('%', #{searchText}, '%')")
    List<TagDTO> selectTagByBlurName(String searchText);

    @Delete("delete from t_tag where id = #{id}")
    int deleteById(Long id);

    List<Group> getGroups();

    @Select("select * from t_tag where is_delete = 0 order by ref_count desc limit 30")
    List<TagDTO> selectHotTags();

    @Select("select * from t_tag where is_delete = 0 and id = #{id}")
    TagDTO selectTagById(Long id);
}
