package com.majiang.community.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author wxh
 * 2023/4/3 13:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends Base {

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


}
