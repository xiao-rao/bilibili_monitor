package com.monitor.bilibili_monitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "查询监控日志对象")
public class QueryMonitorLogDTO implements Serializable {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "来源名称（作者名称/作品名称）")
    private String sourceName;

    @Schema(description = "类型（作者；1，作品；2）")
    private Integer type;

    @Schema(description = "状态（成功；1，失败；0")
    private Integer status;

    @Schema(description = "状态（成功；1，失败；0")
    private Integer isAdd;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;

    @Schema(description = "当前第几页")
    private Integer pageNo;

    @Schema(description = "每页显示条数")
    private Integer pageSize;
}
