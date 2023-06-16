package com.monitor.bilibili_monitor.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.monitor.bilibili_monitor.infra.MonitorLogPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@InterceptorIgnore(illegalSql = "true")
public interface MonitorLOgMapper extends BaseMapper<MonitorLogPO> {
}
