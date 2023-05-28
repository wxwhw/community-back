package com.majiang.community.dto;

import com.majiang.community.pojo.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author wxh
 * 2023/4/29 0:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagMappingDTO extends Base {

    // 标签名称
    private String name;

    // 所属分组
    private String groupName;

    // 标签描述
    private String description;

    // 删除标识 0 未删除 1 已删除
    private Byte isDelete;

    // 审核状态
    private String auditState;

    // 创建人ID
    private Long creatorId;

    // 引用统计
    private Long refCount;

    private List<ArticleDTO> articleList;
}
