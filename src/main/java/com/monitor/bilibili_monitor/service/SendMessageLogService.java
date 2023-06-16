package com.monitor.bilibili_monitor.service;


import com.monitor.bilibili_monitor.dto.QuerySendMessageLogDTO;
import com.monitor.bilibili_monitor.infra.SendMessageLogPO;
import com.monitor.bilibili_monitor.vo.SendMessageLogVO;

import java.util.List;

public interface SendMessageLogService {

    boolean batchSave(List<SendMessageLogPO> list);

    boolean updateSend(SendMessageLogPO po);

    List<SendMessageLogVO> findAll(QuerySendMessageLogDTO dto);

}
