package com.monitor.bilibili_monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "查询b站登陆账号信息")
public class UserVO implements Serializable {


    @Schema(description = "主键ID")
    private Long id;

//    @Schema(description = "账号ID)")
//    private Long userId;

    @Schema(description = "账号昵称")
    private String userName;

    @Schema(description = "账号Cookie")
    private String userCookie;

    @Schema(description = "账号是否有效：1 - 有效，0 - 无效")
    private Integer isValid;

    @Schema(description = "状态：1 - 启用，0 - 禁用")
    private Integer status;

    @Schema(description = "回复内容")
    private String replyContent;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
