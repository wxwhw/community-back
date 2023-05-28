package com.majiang.community.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/4/28 20:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagActicleMapping extends Base{

    /*标签Id*/
    private Long tagId;

    /*文章Id*/
    private Long articleId;

    /*删除标识*/
    private Byte isDelete;
}
