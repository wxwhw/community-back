package com.majiang.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/5/18 22:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserDTO {

    private Long id;

    private String avatar;

    private String nickname;

    private String signature;

}
