package com.monitor.bilibili_monitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "查询私信日志对象")
public class QuerySendMessageLogDTO  implements Serializable {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "日志ID")
    private Long logId;

    @Schema(description = "发送人名称")
    private String senderName;

    @Schema(description = "接收人ID")
    private String receiverId;

    @Schema(description = "状态 (0: 失败, 1: 成功)")
    private Integer status;

    @Schema(description = "日志id数组")
    private List<Long> logIds;
}
