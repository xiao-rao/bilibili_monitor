package com.monitor.bilibili_monitor.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monitor.bilibili_monitor.dto.AccountDTO;
import com.monitor.bilibili_monitor.dto.QueryAccountDTO;
import com.monitor.bilibili_monitor.infra.AccountPO;
import com.monitor.bilibili_monitor.vo.AccountVO;

import java.util.List;

public interface AccountService {

    boolean create(AccountDTO dto);

    boolean updateType(Long id, Integer type);

    boolean update(AccountPO po);

    boolean delete(Long id);

    Page<AccountVO> findPage(QueryAccountDTO dto);

    List<AccountVO> findAll(QueryAccountDTO dto);

    AccountVO findOne(QueryAccountDTO dto);
}
