package com.monitor.bilibili_monitor.util;


import com.monitor.bilibili_monitor.service.BiliBiliApiService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;


@Data
@Slf4j
@Component
@PropertySource("classpath:/task_config.ini")
public class ScheduleTask implements SchedulingConfigurer {


    //    private static final String CONFIG_FILE_PATH = "classpath:/task_config.ini";
    @Value("${monitor_author}")
    private Long monitor_author;
//    private Long timer = 10000L;

    @Value("${monitor_work}")
    private Long monitor_work;

    @Autowired
    private BiliBiliApiService biliBiliApiService;


    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
                    log.info("监听作者任务： {}", LocalDateTime.now());
                biliBiliApiService.monitorTheAuthor();
                }, triggerContext -> {
                    PeriodicTrigger periodicTrigger = new PeriodicTrigger(monitor_author);
                    return periodicTrigger.nextExecutionTime(triggerContext);
                }
        );
        scheduledTaskRegistrar.addTriggerTask(() -> {
                    log.info("监听作品任务： {}", LocalDateTime.now());
                biliBiliApiService.monitorTheWorks();
                }, triggerContext -> {
                    PeriodicTrigger periodicTrigger = new PeriodicTrigger(monitor_work);
                    return periodicTrigger.nextExecutionTime(triggerContext);
                }
        );
    }


}
