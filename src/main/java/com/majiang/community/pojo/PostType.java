package com.majiang.community.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/4/4 13:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostType extends Base{

    // 分类名称
    private String name;

    // 所属作用域
    private String scope;

    // 分类描述
    private String description;

    // 删除标识 0 未删除 1 已删除
    private Byte isDelete;

    // 审核状态
    private String auditState;

    // 创建人ID
    private Long creatorId;

    // 引用统计
    private Long refCount;
}
