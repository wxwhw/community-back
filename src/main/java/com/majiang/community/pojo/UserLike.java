package com.majiang.community.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/5/19 0:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLike extends Base{

    private Long userId;

    private Long articleId;

    private Byte isDelete;

}
