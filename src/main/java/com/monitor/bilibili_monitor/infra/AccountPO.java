package com.monitor.bilibili_monitor.infra;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("b_account")
@EqualsAndHashCode(callSuper = true)
public class AccountPO extends Model<AccountPO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号id
     */
    private String accountId;

    /**
     * 账号昵称
     */
    private String accountNickname;

    /**
     * 作品数量
     */
    private Integer accountWorksCount;

    /**
     * 是否监控 1；是 0；否
     */
    private Integer isMonitored;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    private LocalDateTime modifiedAt;
}
