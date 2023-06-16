package com.monitor.bilibili_monitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "添加账号信息对象")
public class AccountDTO implements Serializable {

    @Schema(description = "账号id")
    private String accountId;

    @Schema(description = "账号昵称")
    private String accountNickname;

    @Schema(description = "作品数量")
    private Integer accountWorksCount;

}
