package com.monitor.bilibili_monitor.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.monitor.bilibili_monitor.infra.TaskConfigPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@InterceptorIgnore(illegalSql = "true")
public interface TaskConfigMapper extends BaseMapper<TaskConfigPO> {

    @Select("select id,`name`,interval_time from b_task_config")
    List<TaskConfigPO> getAll();

    @Update("update b_task_config set interval_time = #{intervalTime} where id = {id}")
    boolean update(Float intervalTime, Long id);
}
