package com.monitor.bilibili_monitor.infra;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("b_send_message_log")
@EqualsAndHashCode(callSuper = true)
public class SendMessageLogPO extends Model<SendMessageLogPO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 日志id
     */
    private Long logId;
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
     * 评论
     */
    private String replies;

    /**
     * 发送信息内容
     */
    private String content;

    /**
     * 状态 0；失败 1；成功
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String errorMsg;

    /**
     * 创建时间(发送消息时间)
     */
    private LocalDateTime createAt;
}
