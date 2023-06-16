package com.monitor.bilibili_monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.monitor.bilibili_monitor.dto.QueryUserDTO;
import com.monitor.bilibili_monitor.infra.UserPO;
import com.monitor.bilibili_monitor.mapper.UserMapper;
import com.monitor.bilibili_monitor.service.UserService;
import com.monitor.bilibili_monitor.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserPO> implements UserService {

    @Autowired
    private UserMapper mapper;

    @Override
    public boolean create(UserPO userPO) {
        return mapper.insert(userPO) > 0;
    }

    @Override
    public boolean update(UserPO userPO) {
        return mapper.updateById(userPO) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }

    @Override
    public List<UserVO> findAll(QueryUserDTO dto) {
        List<UserVO> list  = new ArrayList<>();
        List<UserPO> userPOS = mapper.selectList(getWrapper(dto));
        userPOS.forEach(item -> {
            UserVO userVO = BeanUtil.copyProperties(item, UserVO.class);
            list.add(userVO);
        });
        return list;
    }

    @Override
    public UserPO findOne(QueryUserDTO dto) {
        return mapper.selectOne(getWrapper(dto));
    }

    private QueryWrapper<UserPO> getWrapper(QueryUserDTO dto){
        QueryWrapper<UserPO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(ObjectUtil.isNotNull(dto.getId()),UserPO::getId,dto.getId())
                .eq(StrUtil.isNotBlank(dto.getUserId()),UserPO::getUserId,dto.getUserId())
                .eq(ObjectUtil.isNotNull(dto.getIsValid()),UserPO::getIsValid,dto.getIsValid())
                .eq(ObjectUtil.isNotNull(dto.getStatus()),UserPO::getStatus,dto.getStatus())
                .like(StrUtil.isNotBlank(dto.getUserName()),UserPO::getUserName,dto.getUserName());
        return wrapper;
    }
}
