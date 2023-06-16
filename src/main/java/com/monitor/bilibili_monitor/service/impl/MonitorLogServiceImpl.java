package com.monitor.bilibili_monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.monitor.bilibili_monitor.dto.QueryMonitorLogDTO;
import com.monitor.bilibili_monitor.infra.MonitorLogPO;
import com.monitor.bilibili_monitor.mapper.MonitorLOgMapper;
import com.monitor.bilibili_monitor.service.MonitorLogService;
import com.monitor.bilibili_monitor.vo.MonitorLogVO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MonitorLogServiceImpl extends ServiceImpl<MonitorLOgMapper, MonitorLogPO> implements MonitorLogService {

//    @Autowired
//    private MonitorLOgMapper mapper;

    @Override
    public boolean saveLOg(MonitorLogPO po) {
        return this.baseMapper.insert(po) > 0;
    }

    @Override
    public Map<String, Long>  batchCreate(List<MonitorLogPO> po) {
        Map<String, Long> map = new HashMap<>();
        boolean b = this.saveBatch(po);
        if(b){
            po.forEach(item -> map.put(item.getSourceId(),item.getId()));
        }
        return map;
    }

    @Override
    public Page<MonitorLogVO> findPage(QueryMonitorLogDTO dto) {
        Page<MonitorLogVO> p = new Page<>();
        Page<MonitorLogPO> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        Page<MonitorLogPO> monitorLogPOPage = this.baseMapper.selectPage(page, getWrapper(dto));
        List<MonitorLogVO> collect = monitorLogPOPage.getRecords().stream().map(iten -> {
            MonitorLogVO monitorLogVO = new MonitorLogVO();
            BeanUtil.copyProperties(iten, monitorLogVO);
            return monitorLogVO;
        }).collect(Collectors.toList());
        BeanUtil.copyProperties(monitorLogPOPage, p);
        p.setRecords(collect);
        return p;
    }

    private QueryWrapper<MonitorLogPO> getWrapper(QueryMonitorLogDTO dto) {
        QueryWrapper<MonitorLogPO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(ObjectUtil.isNotNull(dto.getId()), MonitorLogPO::getId, dto.getId())
                .eq(ObjectUtil.isNotNull(dto.getType()), MonitorLogPO::getType, dto.getType())
                .eq(ObjectUtil.isNotNull(dto.getStatus()), MonitorLogPO::getStatus, dto.getStatus())
                .eq(ObjectUtil.isNotNull(dto.getStatus()), MonitorLogPO::getStatus, dto.getStatus())
                .gt(ObjectUtil.isNotNull(dto.getIsAdd()) && 1 == dto.getIsAdd(), MonitorLogPO::getMessage,0)
                .like(StrUtil.isNotBlank(dto.getSourceName()), MonitorLogPO::getSourceName, dto.getSourceName())
                .ge(StrUtil.isNotBlank(dto.getStartTime()), MonitorLogPO::getCreatedAt, dto.getStartTime())
                .le(StrUtil.isNotBlank(dto.getEndTime()), MonitorLogPO::getCreatedAt, dto.getEndTime())
                .orderByDesc(MonitorLogPO::getCreatedAt)
        ;
        return wrapper;
    }


}
