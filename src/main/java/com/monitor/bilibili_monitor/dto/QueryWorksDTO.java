package com.monitor.bilibili_monitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "查询作品信息对象")
public class QueryWorksDTO implements Serializable {

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

    @Schema(description = "当前第几页")
    private Integer pageNo;

    @Schema(description = "每页显示条数")
    private Integer pageSize;

    @Schema(description = "id数组")
    private List<Long> idList;
}
