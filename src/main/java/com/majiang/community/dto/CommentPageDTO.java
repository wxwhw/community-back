package com.majiang.community.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.majiang.community.pojo.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/5/15 20:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentPageDTO extends Base {

    /*根评论id*/
    private Long rootId;

    /*用户对象*/
    private UserDTO user;

    /*文章id*/
    private Long postId;

    /*评论内容*/
    private String content;

    /*回复目标用户id*/
    private Long toUserId;

    /*删除标识*/
    @JsonIgnore
    private Byte isDelete;
}
