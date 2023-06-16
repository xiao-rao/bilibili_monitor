package com.monitor.bilibili_monitor.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monitor.bilibili_monitor.dto.QueryAccountDTO;
import com.monitor.bilibili_monitor.service.AccountService;
import com.monitor.bilibili_monitor.service.BiliBiliApiService;
import com.monitor.bilibili_monitor.service.UserService;
import com.monitor.bilibili_monitor.util.MessageCodeEnum;
import com.monitor.bilibili_monitor.util.ResultDTO;
import com.monitor.bilibili_monitor.vo.AccountVO;
import com.monitor.bilibili_monitor.vo.WorksVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/accounts")
@Api(tags = "账号管理API")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService service;
    @Autowired
    private BiliBiliApiService biliBiliApiService;

    @Autowired
    private UserService userService;

    @ApiOperation("查询监听账号信息")
    @GetMapping("/")
    public ResultDTO<?> findAll(
            @Parameter(description = "查询关键字") @RequestParam(value = "key", required = false) String key,
            @Parameter(description = "是否监听 1；是 0；否", example = "0") @RequestParam(value = "type", required = false) Integer type,
            @Parameter(description = "当前第几页", required = true, example = "1") @RequestParam(value = "pageNo") Integer pageNo,
            @Parameter(description = "每页数据条数", required = true, example = "10") @RequestParam(value = "pageSize") Integer pageSize
    ) {
        ResultDTO<Page<AccountVO>> resultDTO = new ResultDTO<>();
        try {
            QueryAccountDTO queryAccountDTO = new QueryAccountDTO();
            queryAccountDTO.setAccountNickname(key);
            queryAccountDTO.setIsMonitored(type);
            queryAccountDTO.setPageNo(pageNo);
            queryAccountDTO.setPageSize(pageSize);
            Page<AccountVO> all = service.findPage(queryAccountDTO);
            resultDTO.setData(all);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("查询监听账号信息出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @GetMapping("/create")
    @Operation(summary = "新增监听账号信息", method = "GET")
    public ResultDTO<List<WorksVO>> createAccount(
            @Parameter(description = "账号id", required = true) @RequestParam(value = "accountId") String accountId
    ) {
        ResultDTO<List<WorksVO>> resultDTO = new ResultDTO<>();
        try {
            List<WorksVO> userWorks = biliBiliApiService.getAuthorInfo(accountId);
            if (CollUtil.isEmpty(userWorks)) {
                throw new IllegalArgumentException("用户不存在或没有更新的作品");
            }
            resultDTO.setData(userWorks);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("新增监听账号信息出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @GetMapping("/change")
    @Operation(summary = "编辑监听账号信息", method = "GET")
    public ResultDTO<Boolean> changeAccount(
            @Parameter(description = "主键id", required = true) @RequestParam(value = "id") Long id,
            @Parameter(description = "是否监听 1；是 0；否", required = true, example = "0") @RequestParam(value = "type") Integer type
    ) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<>();
        try {
            boolean b = service.updateType(id, type);
            resultDTO.setData(b);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("监听账号信息-编辑出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @GetMapping("/delete")
    @Operation(summary = "删除监听账号信息", method = "Get")
    public ResultDTO<Boolean> deleteAccount(
            @Parameter(description = "主键id", required = true) @RequestParam(value = "id") Long id
    ) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<>();
        try {
            boolean b = service.delete(id);
            resultDTO.setData(b);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("监听账号信息-删除出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }
}
