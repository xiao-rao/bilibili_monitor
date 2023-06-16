package com.monitor.bilibili_monitor.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.monitor.bilibili_monitor.infra.WorksPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@InterceptorIgnore(illegalSql = "true")
public interface WorksMapper extends BaseMapper<WorksPO> {

}
