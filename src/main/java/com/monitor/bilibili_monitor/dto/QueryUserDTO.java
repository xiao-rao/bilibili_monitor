package com.monitor.bilibili_monitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QueryUserDTO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "账号ID)")
    private String userId;

    @Schema(description = "账号昵称")
    private String userName;

    @Schema(description = "账号是否有效：1 - 有效，0 - 无效")
    private Integer isValid;

    @Schema(description = "状态：1 - 启用，0 - 禁用")
    private Integer status;
}
