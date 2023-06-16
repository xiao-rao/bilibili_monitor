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
@TableName("b_user")
@EqualsAndHashCode(callSuper = true)
public class UserPO extends Model<UserPO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 主键ID
     */
    private String userId;
    /**
     * 账号ID
     */
    private String userName;
    /**
     * 账号昵称
     */
    private String userCookie;
    /**
     * 账号Cookie
     */
    private Integer isValid;
    /**
     * 账号是否有效：1 - 有效，0 - 无效
     */
    private String replyContent;

    /**
     * 状态：1 - 启用，0 - 禁用
     */
    private Integer status;
    /**
     * 回复内容
     */
    private LocalDateTime createdAt;
    /**
     * 添加时间
     */
    private LocalDateTime updatedAt;
    /**
     * 修改时间
     */
}
