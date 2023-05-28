package com.majiang.community.mapper;

import com.majiang.community.pojo.Carousel;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.net.Inet4Address;
import java.util.List;

/**
 * Author wxh
 * 2023/5/17 20:35
 */
public interface CarouselMappper {

    Integer insertCarousel(Carousel carousel);

    List<Carousel> selectCarouselPage(String auditState, String type, String title, Integer offset, Integer pageSize);

    @Select("select type from t_carousel where is_delete = 0 group by type")
    List<String> selectTypeList();

    @Update("update t_carousel set audit_state = #{newState} where id = #{id}")
    Integer updateAuditState(Long id, String newState);

    @Update("update t_carousel set is_delete = 1 where id = #{id}")
    Integer deleteById(Long id);

    Long selectTotal(String auditState, String type, String title);

    List<Carousel> selectCarouselList();
}
