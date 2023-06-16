package com.monitor.bilibili_monitor.infra;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("b_works")
@EqualsAndHashCode(callSuper = true)
public class WorksPO extends Model<WorksPO> {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 作品id
     */
    private String workId;

    /**
     * 作品标题
     */
    private String workTitle;

    /**
     * 作品评论数
     */
    private Integer workCommentCount;

    /**
     * 是否监控 (1为是, 0为否)
     */
    private Integer isMonitored;

    /**
     * 作者id
     */
    private String authorId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    private LocalDateTime modifiedAt;
}
