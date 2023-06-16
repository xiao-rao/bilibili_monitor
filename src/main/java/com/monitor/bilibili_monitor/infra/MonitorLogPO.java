package com.monitor.bilibili_monitor.infra;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("b_monitor_log")
@EqualsAndHashCode(callSuper = true)
public class MonitorLogPO extends Model<MonitorLogPO> {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 来源id（作者id/作品id）
     */
    private String sourceId;

    /**
     * 来源名称（作者名称/作品名称）
     */
    private String sourceName;

    /**
     * 类型（作者；1，作品；2）
     */
    private Integer type;

    /**
     * 类型（成功；1，失败；0）
     */
    private Integer status;

    /**
     * 消息
     */
    private String Message;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
