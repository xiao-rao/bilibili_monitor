package com.monitor.bilibili_monitor.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.monitor.bilibili_monitor.dto.QuerySendMessageLogDTO;
import com.monitor.bilibili_monitor.infra.MonitorLogPO;
import com.monitor.bilibili_monitor.infra.SendMessageLogPO;
import com.monitor.bilibili_monitor.mapper.SendMessageLogMapper;
import com.monitor.bilibili_monitor.service.SendMessageLogService;
import com.monitor.bilibili_monitor.vo.SendMessageLogVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SendMessageLogServiceImpl extends ServiceImpl<SendMessageLogMapper, SendMessageLogPO> implements SendMessageLogService {


    @Override
    public boolean batchSave(List<SendMessageLogPO> list) {
        return this.saveBatch(list);
    }

    @Override
    public boolean updateSend(SendMessageLogPO po) {
        return this.updateById(po);
    }

    @Override
    public List<SendMessageLogVO> findAll(QuerySendMessageLogDTO dto) {
        List<SendMessageLogPO> sendMessageLogPOList = this.baseMapper.selectList(getWrapper(dto));
        List<SendMessageLogVO> list = new ArrayList<>();
        sendMessageLogPOList.forEach(item -> list.add(BeanUtil.copyProperties(item, SendMessageLogVO.class)));
        return list;
    }


    private QueryWrapper<SendMessageLogPO> getWrapper(QuerySendMessageLogDTO dto){
        QueryWrapper<SendMessageLogPO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(ObjectUtil.isNotNull(dto.getId()), SendMessageLogPO::getId, dto.getId())
                .eq(ObjectUtil.isNotNull(dto.getLogId()), SendMessageLogPO::getLogId, dto.getLogId())
                .eq(ObjectUtil.isNotNull(dto.getStatus()), SendMessageLogPO::getStatus, dto.getStatus())
                .like(StrUtil.isNotBlank(dto.getSenderName()), SendMessageLogPO::getSenderName, dto.getSenderName())
                .in(CollUtil.isNotEmpty(dto.getLogIds()),SendMessageLogPO::getLogId,dto.getLogIds())
        ;
//                .ge(StrUtil.isNotBlank(dto.getStartTime()), MonitorLogPO::getCreatedAt, dto.getStartTime())
//                .le(StrUtil.isNotBlank(dto.getEndTime()), MonitorLogPO::getCreatedAt, dto.getEndTime())
        return wrapper;
    }
}
