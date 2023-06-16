package com.monitor.bilibili_monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "账号信息返回对象")
public class AccountVO implements Serializable {

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

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "最后一次修改时间 (更新时间)")
    private LocalDateTime modifiedAt;
}
