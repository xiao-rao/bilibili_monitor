package com.monitor.bilibili_monitor.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.monitor.bilibili_monitor.infra.AccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

//@Repository
@Mapper
@InterceptorIgnore(illegalSql = "true")
public interface AccountMapper extends BaseMapper<AccountPO> {
}
