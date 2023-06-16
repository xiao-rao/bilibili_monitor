package com.monitor.bilibili_monitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "账号信息查询对象")
public class QueryAccountDTO implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "账号id")
    private String accountId;

    @Schema(description = "账号昵称")
    private String accountNickname;

    @Schema(description = "作品数量")
    private Integer accountWorksCount;

    @Schema(description = " 是否监控 1；是 0；否")
    private Integer isMonitored;

    @Schema(description = "当前第几页")
    private Integer pageNo;

    @Schema(description = "每页显示条数")
    private Integer pageSize;
}
