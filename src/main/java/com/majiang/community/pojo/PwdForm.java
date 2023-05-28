package com.majiang.community.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/5/18 23:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PwdForm {

    private Long userId;

    private String oldPwd;

    private String newPwd;

    private String repeatPwd;
}
