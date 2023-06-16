package com.monitor.bilibili_monitor.infra;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("b_task_config")
@EqualsAndHashCode(callSuper = true)
public class TaskConfigPO extends Model<TaskConfigPO> {

    private Long id;

    private String name;

    private Float intervalTime;
}
