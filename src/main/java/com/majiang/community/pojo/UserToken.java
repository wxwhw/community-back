package com.majiang.community.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/3/5 18:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {

    /*tokenID*/
    private Long id;

    /*token*/
    private String token;

    /*用户ID*/
    private User user;
}
