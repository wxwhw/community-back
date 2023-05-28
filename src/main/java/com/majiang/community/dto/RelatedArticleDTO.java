package com.majiang.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/5/18 0:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatedArticleDTO {

    private Long id;

    private String title;

    private String coverUrl;
}
