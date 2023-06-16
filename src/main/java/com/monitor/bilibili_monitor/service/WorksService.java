package com.monitor.bilibili_monitor.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monitor.bilibili_monitor.dto.QueryWorksDTO;
import com.monitor.bilibili_monitor.dto.WorksDTO;
import com.monitor.bilibili_monitor.infra.WorksPO;
import com.monitor.bilibili_monitor.vo.WorksVO;

import java.util.List;

public interface WorksService {

    boolean create(WorksDTO dto);

    List<WorksVO> batchCreate(List<WorksDTO> dtos);

    boolean update(WorksPO worksPO);

    boolean batchUpdate(List<Long> ids, Integer type);

    boolean delete(List<Long> id);

    Page<WorksVO> findAll(QueryWorksDTO dto);

    Integer count(QueryWorksDTO dto);

    List<WorksVO> queryAll(QueryWorksDTO dto);
}
