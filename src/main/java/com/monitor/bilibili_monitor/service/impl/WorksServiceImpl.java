package com.monitor.bilibili_monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.monitor.bilibili_monitor.dto.QueryWorksDTO;
import com.monitor.bilibili_monitor.dto.WorksDTO;
import com.monitor.bilibili_monitor.infra.WorksPO;
import com.monitor.bilibili_monitor.mapper.WorksMapper;
import com.monitor.bilibili_monitor.service.WorksService;
import com.monitor.bilibili_monitor.vo.WorksVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorksServiceImpl extends ServiceImpl<WorksMapper, WorksPO> implements WorksService {

    @Autowired
    private WorksMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(WorksDTO dto) {
        WorksPO worksPO = BeanUtil.copyProperties(dto, WorksPO.class);
        worksPO.setCreatedAt(LocalDateTime.now());
       return mapper.insert(worksPO) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WorksVO> batchCreate(List<WorksDTO> dtos) {
        List<WorksVO> accountVOS = new ArrayList<>();
        List<WorksPO> collect = dtos.stream().map(item -> {
            WorksPO worksPO = BeanUtil.copyProperties(item, WorksPO.class);
            worksPO.setCreatedAt(LocalDateTime.now());
            worksPO.setModifiedAt(LocalDateTime.now());
            return worksPO;
        }).collect(Collectors.toList());
        if (this.saveBatch(collect)) {
            collect.forEach(item -> {
                WorksVO worksVO = BeanUtil.copyProperties(item, WorksVO.class);
                accountVOS.add(worksVO);
            });
        }
        return accountVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update( WorksPO worksPO) {
        worksPO.setModifiedAt(LocalDateTime.now());
        return mapper.updateById(worksPO) > 0;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<Long> ids, Integer type) {
        UpdateWrapper<WorksPO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(WorksPO::getIsMonitored, type)
                .set(WorksPO::getModifiedAt, LocalDateTime.now())
                .in(WorksPO::getId, ids);
        return mapper.update(new WorksPO(), updateWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Long> id) {
        QueryWorksDTO queryWorksDTO = new QueryWorksDTO();
        queryWorksDTO.setIdList(id);
        return mapper.delete(getWrapper(queryWorksDTO)) > 0;
    }

    @Override
    public Page<WorksVO> findAll(QueryWorksDTO dto) {
        Page<WorksVO> p = new Page<>();
        Page<WorksPO> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        Page<WorksPO> accountPOPage = mapper.selectPage(page, getWrapper(dto));
        List<WorksVO> collect = accountPOPage.getRecords().stream().map(iten -> {
            WorksVO worksVO = new WorksVO();
            BeanUtil.copyProperties(iten, worksVO);
            return worksVO;
        }).collect(Collectors.toList());
        BeanUtil.copyProperties(accountPOPage, p);
        p.setRecords(collect);
        return p;
    }

    @Override
    public Integer count(QueryWorksDTO dto) {
        return mapper.selectCount(getWrapper(dto));
    }

    @Override
    public List<WorksVO> queryAll(QueryWorksDTO dto) {
        List<WorksVO> list= new ArrayList<>();
        List<WorksPO> worksPOS = mapper.selectList(getWrapper(dto));
        worksPOS.forEach(item -> list.add(BeanUtil.copyProperties(item, WorksVO.class)));
        return list;
    }

    public QueryWrapper<WorksPO> getWrapper(QueryWorksDTO dto) {
        QueryWrapper<WorksPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ObjectUtil.isNotNull(dto.getIsMonitored()), WorksPO::getIsMonitored, dto.getIsMonitored())
                .eq(ObjectUtil.isNotNull(dto.getId()), WorksPO::getId, dto.getId())
                .eq(StrUtil.isNotBlank(dto.getAuthorId()), WorksPO::getAuthorId, dto.getAuthorId())
                .eq(StrUtil.isNotBlank(dto.getWorkId()), WorksPO::getWorkId, dto.getWorkId())
                .like(StrUtil.isNotBlank(dto.getWorkTitle()), WorksPO::getWorkTitle, dto.getWorkTitle())
                .in(CollUtil.isNotEmpty(dto.getIdList()), WorksPO::getId, dto.getIdList())
        ;
        return queryWrapper;
    }
}
