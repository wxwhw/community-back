package com.majiang.community.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.majiang.community.pojo.Base;
import com.majiang.community.pojo.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author wxh
 * 2023/4/28 23:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO extends Base {

    /*文章标题*/
    private String title;

    /*审核状态*/
    private String auditState;

    /*解析得到的html内容*/
    private String htmlContent;

    /*markdown内容*/
    private String markdownContent;

    /*作者ID*/
    @JsonIgnore
    private Long userId;

    /*封装作者对象*/
    private UserDTO user;

    /*浏览量*/
    private Long views;

    /*点赞量*/
    private Long approvals;

    /*评论量*/
    private Long comments;

    /*社区ID*/
    @JsonIgnore
    private Long typeId;

    /*社区类型*/
    private ArticleTypeDTO articleType;

    /*文章标签ID数组*/
    private List<TagDTO> tagList;

    /*文章封面地址*/
    private String coverUrl;

    /*是否官方文章*/
    private Byte official;

    /*是否置顶*/
    private Byte top;

    /*排序*/
    private Integer sort;

    /*收藏量*/
    private Long collections;

    /*删除标识*/
    private Byte isDelete;

    /*是否精华*/
    private Byte marrow;
}
