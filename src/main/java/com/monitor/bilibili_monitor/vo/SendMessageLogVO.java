package com.monitor.bilibili_monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "发送私信日志返回对象")
public class SendMessageLogVO implements Serializable {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "日志ID")
    private Long logId;

    @Schema(description = "发送人名称")
    private String senderName;
//
    @Schema(description = "接收人ID")
    private String receiverId;

    @Schema(description = "接收人名称")
    private String receiverName;

    @Schema(description = "评论")
    private String replies;

    @Schema(description = "发送信息内容")
    private String content;

    @Schema(description = "状态 (0: 失败, 1: 成功)")
    private Integer status;

    @Schema(description = "失败原因")
    private String errorMsg;

    @Schema(description = "创建时间(发送消息时间)")
    private LocalDateTime createAt;

}
