package com.monitor.bilibili_monitor.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monitor.bilibili_monitor.dto.QueryMonitorLogDTO;
import com.monitor.bilibili_monitor.infra.MonitorLogPO;
import com.monitor.bilibili_monitor.vo.MonitorLogVO;

import java.util.List;
import java.util.Map;

public interface MonitorLogService {


    boolean saveLOg(MonitorLogPO po);

    Map<String, Long> batchCreate(List<MonitorLogPO> po);

    Page<MonitorLogVO> findPage(QueryMonitorLogDTO dto);
}
