package com.monitor.bilibili_monitor.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monitor.bilibili_monitor.dto.QueryMonitorLogDTO;
import com.monitor.bilibili_monitor.dto.QuerySendMessageLogDTO;
import com.monitor.bilibili_monitor.service.BiliBiliApiService;
import com.monitor.bilibili_monitor.service.MonitorLogService;
import com.monitor.bilibili_monitor.service.SendMessageLogService;
import com.monitor.bilibili_monitor.util.Constant;
import com.monitor.bilibili_monitor.util.MessageCodeEnum;
import com.monitor.bilibili_monitor.util.ResultDTO;
import com.monitor.bilibili_monitor.vo.MonitorLogVO;
import com.monitor.bilibili_monitor.vo.SendMessageLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/log")
@Api(tags = "监听日志操作API")
@Slf4j
public class MonitorLogController {

    @Autowired
    private MonitorLogService logService;

    @Autowired
    private SendMessageLogService messageLogService;
    @Autowired
    private BiliBiliApiService service;

    @ApiOperation("查询日志")
    @GetMapping("/")
    public ResultDTO<Page<MonitorLogVO>> getAccountLOg(
            @Parameter(description = "查询关键字") @RequestParam(value = "key", required = false) String key,
            @Parameter(description = "类型 1；作者 2；作品", required = true) @RequestParam(value = "type") Integer type,
            @Parameter(description = "状态 1；成功 0；失败") @RequestParam(value = "status", required = false) Integer status,
            @Parameter(description = "状态 1；有新增 0；无新增") @RequestParam(value = "isAdd", required = false) Integer isAdd,
            @Parameter(description = "开始时间") @RequestParam(value = "startTime", required = false) String startTime,
            @Parameter(description = "结束时间") @RequestParam(value = "endTime", required = false) String endTime,
            @Parameter(description = "当前第几页", required = true, example = "1") @RequestParam(value = "pageNo") Integer pageNo,
            @Parameter(description = "每页数据条数", required = true, example = "10") @RequestParam(value = "pageSize") Integer pageSize
    ) {
        ResultDTO<Page<MonitorLogVO>> resultDTO = new ResultDTO<>();
        try {
            QueryMonitorLogDTO queryMonitorLogDTO = new QueryMonitorLogDTO();
            queryMonitorLogDTO.setPageNo(pageNo);
            queryMonitorLogDTO.setPageSize(pageSize);
            queryMonitorLogDTO.setSourceName(key);
            queryMonitorLogDTO.setType(type);
            queryMonitorLogDTO.setStatus(status);
            queryMonitorLogDTO.setIsAdd(isAdd);
            queryMonitorLogDTO.setStartTime(startTime);
            queryMonitorLogDTO.setEndTime(endTime);
            Page<MonitorLogVO> page = logService.findPage(queryMonitorLogDTO);
            if (Objects.equals(type, Constant.WORKS)) {
                List<Long> ids = new ArrayList<>();
                List<MonitorLogVO> records = page.getRecords();
                records.forEach(item -> ids.add(item.getId()));
                Map<Long, List<SendMessageLogVO>> map = getMap(ids);
                records.forEach(item -> item.setSendMessageLogVOList(map.get(item.getId())));
            }
            resultDTO.setData(page);
        }catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("查询日志出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }

    @ApiOperation("重新发送私信")
    @PostMapping("/re/send")
    public ResultDTO<String> reSendMEssage(
            @RequestBody SendMessageLogVO vo
    ){
        if (null == vo){
            return ResultDTO.fail("请求参数出错！");
        }
        return service.reSendMessage(vo);
    }

    private Map<Long, List<SendMessageLogVO>> getMap(List<Long> ids) {
        Map<Long, List<SendMessageLogVO>> map = new HashMap<>();
        QuerySendMessageLogDTO querySendMessageLogDTO = new QuerySendMessageLogDTO();
        querySendMessageLogDTO.setLogIds(ids);
        List<SendMessageLogVO> all = messageLogService.findAll(querySendMessageLogDTO);
        all.forEach(item -> map.computeIfAbsent(item.getLogId(), k -> new ArrayList<>()).add(item));
        return map;
    }
}
