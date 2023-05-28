package com.majiang.community.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Author wxh
 * 2023/5/17 20:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carousel extends Base{

    /*审核状态*/
    private String auditState;

    /*标题*/
    private String title;

    /*类型：文章、活动、广告*/
    private String type;

    /*图片链接*/
    private String imgUrl;

    /*跳转链接*/
    private String actUrl;

    /*创建人Id*/
    private Long creator;

    /*激活时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startAt;

    /*失效时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endAt;

    /*删除标识*/
    private Byte isDelete;

}
