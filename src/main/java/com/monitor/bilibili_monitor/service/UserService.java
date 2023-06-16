package com.monitor.bilibili_monitor.service;

import com.monitor.bilibili_monitor.dto.QueryUserDTO;
import com.monitor.bilibili_monitor.infra.UserPO;
import com.monitor.bilibili_monitor.vo.UserVO;
import io.swagger.models.auth.In;

import java.util.List;

public interface UserService {

    boolean create(UserPO userPO);

    boolean update(UserPO userPO);

    boolean delete(Long id);

    List<UserVO> findAll(QueryUserDTO dto);

    UserPO findOne(QueryUserDTO dto);
}
