package com.majiang.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/4/29 13:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTypeDTO {

    private Long id;

    private String name;

    private String description;

    private String scope;

    private Long refCount;
}
