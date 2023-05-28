package com.majiang.community.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.majiang.community.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author wxh
 * 2023/4/30 22:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends Base{

    /*根评论id*/
    private Long rootId;

    /*用户id*/
    private Long userId;

    /*文章id*/
    private Long postId;

    /*评论内容*/
    private String content;

    /*回复目标用户id*/
    private Long toUserId;

    /*删除标识*/
    @JsonIgnore
    private Byte isDelete;

    /*二级评论列表*/
    private List<Comment> subComList;

    /*用户对象,和userId对应*/
    private UserDTO user;

    /*被回复用户对象,和toCommentId对应*/
    private UserDTO toUser;
}
