package com.majiang.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.dc.pr.PRError;

/**
 * Author wxh
 * 2023/4/8 21:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    private Long id;

    private String name;

    private String groupName;

    private String refCount;

    private String description;
}
