package com.monitor.bilibili_monitor.controller;

import com.monitor.bilibili_monitor.util.MessageCodeEnum;
import com.monitor.bilibili_monitor.util.ResultDTO;
import com.monitor.bilibili_monitor.util.ScheduleTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "监听配置操作API")
@RequestMapping("/api/task")
public class MonitorTaskController {

    private final ScheduleTask scheduleTask;

    @Autowired
    public MonitorTaskController(ScheduleTask scheduleTask) {
        this.scheduleTask = scheduleTask;
    }

    @GetMapping("/updateTimer/author")
    @ApiOperation("修改监听任务时间")
    public ResultDTO<String> updateTimer(Long timer) {
        try {
            log.info("new timer :{}", timer);
            scheduleTask.setMonitor_author(timer);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("修改监听时间日志出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return ResultDTO.success("ok");
    }

    @GetMapping("/updateTimer/works")
    @ApiOperation("修改监听作品任务时间")
    public ResultDTO<String> updateTimerWorks(Long timer) {
        try {
            log.info("new timer :{}", timer);
            scheduleTask.setMonitor_work(timer);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("修改监听时间日志出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return ResultDTO.success("ok");
    }

    @GetMapping("/find")
    @ApiOperation("查询监听作品任务时间")
    public ResultDTO<Map<String, Long>> find() {
        ResultDTO<Map<String, Long>> resultDTO = new ResultDTO<>();
        try {
            Map<String, Long> map = new HashMap<>();
            map.put("monitor_author", scheduleTask.getMonitor_author());
            map.put("monitor_work", scheduleTask.getMonitor_work());
            resultDTO.setData(map);
        } catch (IllegalArgumentException e) {
            return ResultDTO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("修改监听时间日志出错:{}", e.getMessage());
            return ResultDTO.fail(MessageCodeEnum.fail.getDesc());
        }
        return resultDTO;
    }
}
