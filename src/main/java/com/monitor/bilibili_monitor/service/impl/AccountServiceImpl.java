package com.monitor.bilibili_monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.monitor.bilibili_monitor.dto.AccountDTO;
import com.monitor.bilibili_monitor.dto.QueryAccountDTO;
import com.monitor.bilibili_monitor.infra.AccountPO;
import com.monitor.bilibili_monitor.mapper.AccountMapper;
import com.monitor.bilibili_monitor.service.AccountService;
import com.monitor.bilibili_monitor.vo.AccountVO;
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
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountPO> implements AccountService {

    @Autowired
    private AccountMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(AccountDTO dto) {
        AccountPO accountPO = BeanUtil.copyProperties(dto, AccountPO.class);
        accountPO.setIsMonitored(0);
        accountPO.setCreatedAt(LocalDateTime.now());
        accountPO.setModifiedAt(LocalDateTime.now());
        return mapper.insert(accountPO) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateType(Long id, Integer type) {
        AccountPO queryAccountDTO = new AccountPO();
        queryAccountDTO.setId(id);
        queryAccountDTO.setIsMonitored(type);
        return mapper.updateById(queryAccountDTO) > 0;
    }

    @Override
    public boolean update(AccountPO po) {
        return mapper.updateById(po) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }

    @Override
    public Page<AccountVO> findPage(QueryAccountDTO dto) {
        Page<AccountVO> p = new Page<>();
        Page<AccountPO> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        Page<AccountPO> accountPOPage = mapper.selectPage(page, getWrapper(dto));
        List<AccountVO> collect = accountPOPage.getRecords().stream().map(iten -> {
            AccountVO accountVO = new AccountVO();
            BeanUtil.copyProperties(iten, accountVO);
            return accountVO;
        }).collect(Collectors.toList());
        BeanUtil.copyProperties(accountPOPage, p);
        p.setRecords(collect);
        return p;
    }

    @Override
    public List<AccountVO> findAll(QueryAccountDTO dto) {
        List<AccountVO> list = new ArrayList<>();
        List<AccountPO> accountPOS = mapper.selectList(getWrapper(dto));
        if (CollUtil.isNotEmpty(accountPOS)){
            accountPOS.forEach(item -> list.add(BeanUtil.copyProperties(item,AccountVO.class)));
        }
        return list;
    }

    @Override
    public AccountVO findOne(QueryAccountDTO dto) {
        AccountPO accountPO = mapper.selectOne(getWrapper(dto));
        if (null != accountPO){
            return BeanUtil.copyProperties(accountPO, AccountVO.class);
        }
        return null;
    }

    public QueryWrapper<AccountPO> getWrapper(QueryAccountDTO dto) {
        QueryWrapper<AccountPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ObjectUtil.isNotNull(dto.getIsMonitored()), AccountPO::getIsMonitored, dto.getIsMonitored())
                .eq(ObjectUtil.isNotNull(dto.getId()), AccountPO::getId, dto.getId())
                .eq(StrUtil.isNotBlank(dto.getAccountId()), AccountPO::getAccountId, dto.getAccountId())
                .like(StrUtil.isNotBlank(dto.getAccountNickname()), AccountPO::getAccountNickname, dto.getAccountNickname())
        ;
        return queryWrapper;
    }
}
