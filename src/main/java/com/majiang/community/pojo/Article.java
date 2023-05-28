package com.majiang.community.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/4/28 15:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article extends Base{

    /*文章标题*/
    private String title;

    /*审核状态*/
    private String auditState;

    /*解析得到的html内容*/
    private String htmlContent;

    /*markdown内容*/
    private String markdownContent;

    /*作者ID*/
    private Long userId;

    /*浏览量*/
    private Long views;

    /*点赞量*/
    private Long approvals;

    /*评论量*/
    private Long comments;

    /*社区ID*/
    private Long typeId;

    /*文章标签ID数组*/
    private Long[] tagIds;

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
