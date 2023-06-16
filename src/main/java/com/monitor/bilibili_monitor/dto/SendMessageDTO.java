package com.monitor.bilibili_monitor.dto;

import com.monitor.bilibili_monitor.infra.BiliBiliCookiePO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "发送私信对象")
public class SendMessageDTO implements Serializable {

    /**
     * 日志id
     */
    private Long logID;

    /**
     * 发送人名称
     */
    private String senderName;

    /**
     * 接收人id
     */
    private String receiverId;

    /**
     * 接收人名称
     */
    private String receiverName;

    /**
     * 发送信息内容
     */
    private String content;

    /**
     * cookie
     */
    private BiliBiliCookiePO biliBiliCookiePO;

}
