package com.monitor.bilibili_monitor.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.monitor.bilibili_monitor.dto.QueryUserDTO;
import com.monitor.bilibili_monitor.infra.UserPO;
import com.monitor.bilibili_monitor.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@InterceptorIgnore(illegalSql = "true")
public interface UserMapper extends BaseMapper<UserPO> {

}
