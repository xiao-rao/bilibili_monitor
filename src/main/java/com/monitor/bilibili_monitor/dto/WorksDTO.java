package com.monitor.bilibili_monitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
@Data
@Schema(description = "添加作品信息对象")
public class WorksDTO implements Serializable {

    @Schema(description = "作品ID")
    private String workId;

    @Schema(description = "作品标题")
    private String workTitle;

    @Schema(description = "作品评论数")
    private Integer workCommentCount;

    @Schema(description = "是否监控 (1为是, 0为否)")
    private Integer isMonitored;

    @Schema(description = "作者ID")
    private String authorId;
}
