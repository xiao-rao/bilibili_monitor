package com.monitor.bilibili_monitor.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.monitor.bilibili_monitor.infra.SendMessageLogPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@InterceptorIgnore(illegalSql = "true")
public interface SendMessageLogMapper extends BaseMapper<SendMessageLogPO> {
}
