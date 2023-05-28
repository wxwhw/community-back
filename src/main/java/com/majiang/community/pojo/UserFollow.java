package com.majiang.community.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/5/4 23:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFollow extends Base{

    Long followed;

    String followType;

    Long userId;

    Byte isDelete;

}
