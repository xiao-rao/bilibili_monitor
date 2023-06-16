package com.monitor.bilibili_monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "监控日志返回对象")
public class MonitorLogVO implements Serializable {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "来源id（作者id/作品id）")
    private Long sourceId;

    @Schema(description = "来源名称（作者名称/作品名称）")
    private String sourceName;

    @Schema(description = "类型（作者；1，作品；2）")
    private Integer type;

    @Schema(description = "状态（成功；1，失败；0")
    private Integer status;

    @Schema(description = "消息")
    private String Message;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "私信日志数组")
    private List<SendMessageLogVO> sendMessageLogVOList;

}
