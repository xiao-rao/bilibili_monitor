package com.monitor.bilibili_monitor.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monitor.bilibili_monitor.dto.QueryWorksDTO;
import com.monitor.bilibili_monitor.dto.WorksDTO;
import com.monitor.bilibili_monitor.infra.WorksPO;
import com.monitor.bilibili_monitor.service.BiliBiliApiService;
import com.monitor.bilibili_monitor.service.WorksService;
import com.monitor.bilibili_monitor.util.MessageCodeEnum;
import com.monitor.bilibili_monitor.util.ResultDTO;
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
@RequestMapping("/api/works")
@Api(tags = "作品管理API")
@Slf4j
public class WorksController {

    @Autowired
    private WorksService service;
    @Autowired
    private BiliBiliApiService biliBiliApiService;

    @ApiOperation("查询监听作品信息")
    @GetMapping("/")
    public ResultDTO<Page<WorksVO>> findAll(
            @Parameter(description = "查询关键字") @RequestParam(value = "key", required = false) String key,
            @Parameter(description = "作者id") @RequestParam(value = "authorId", required = false) String authorId,
            @Parameter(description = "是否监听 1；是 0；否") @RequestParam(value = "type", required = false) Integer type,
            @Parameter(description = "当前第几页", required = true, example = "1") @RequestParam(value = "pageNo") Integer pageNo,
            @Parameter(description = "每页数据条数", required = true, example = "10") @RequestParam(value = "pageSize") Integer pageSize
    ) {
        ResultDTO<Page<WorksVO>> resultDTO = new ResultDTO<>();
        try {
            QueryWorksDTO queryWorksDTO = new QueryWorksDTO();
            queryWorksDTO.setWorkTitle(key);
            queryWorksDTO.setAuthorId(authorId);
            queryWorksDTO.setIsMonitored(type);
            queryWorksDTO.setPageNo(pageNo);
            queryWorksDTO.setPageSize(pageSize);
            Page<WorksVO> all = service.findAll(queryWorksDTO);
            resultDTO.setData(all);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("查询监听作品信息出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @GetMapping("/create")
    @Operation(summary = "新增监听作品信息", method = "GET")
    public ResultDTO<Boolean> createWorks(
            @Parameter(description = "作品bvid", required = true) @RequestParam(value = "bvid") String bvid
    ) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<>();
        try {
            boolean b = biliBiliApiService.getWorksInfo(bvid);
            resultDTO.setData(b);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("新增监听作品信息出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @GetMapping("/change")
    @Operation(summary = "编辑监听作品信息", method = "GET")
    public ResultDTO<Boolean> changeWorks(
            @Parameter(description = "主键id", required = true) @RequestParam(value = "id") Long id,
            @Parameter(description = "是否监听 1；是 0；否", required = true, example = "0") @RequestParam(value = "type") Integer type
    ) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<>();
        try {
            WorksPO worksPO = new WorksPO();
            worksPO.setId(id);
            worksPO.setIsMonitored(type);
            boolean b = service.update(worksPO);
            resultDTO.setData(b);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("监听作品信息-编辑出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @GetMapping("/change/batch")
    @Operation(summary = "批量编辑监听作品信息", method = "GET")
    public ResultDTO<Boolean> batchChangeWorks(
            @Parameter(description = "主键id数组", required = true) @RequestParam(value = "ids") List<Long> ids,
            @Parameter(description = "是否监听 1；是 0；否", required = true, example = "0") @RequestParam(value = "type") Integer type
    ) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<>();
        try {
            boolean b = service.batchUpdate(ids, type);
            resultDTO.setData(b);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("监听作品信息-编辑出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @GetMapping("/delete")
    @Operation(summary = "批量删除监听作品信息", method = "Get")
    public ResultDTO<Boolean> deleteWorks(
            @Parameter(description = "主键id", required = true) @RequestParam(value = "id") List<Long> id
    ) {
        ResultDTO<Boolean> resultDTO = new ResultDTO<>();
        try {
            boolean b = service.delete(id);
            resultDTO.setData(b);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("监听作品信息-删除出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }
}
