package com.monitor.bilibili_monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "作品信息返回对象")
public class WorksVO implements Serializable {

    @Schema(description = "id")
    private Long id;

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

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "最后一次修改时间")
    private LocalDateTime modifiedAt;
}
