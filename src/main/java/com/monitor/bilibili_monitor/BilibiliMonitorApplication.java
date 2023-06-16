package com.monitor.bilibili_monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class BilibiliMonitorApplication {

    public static void main(String[] args) {

        SpringApplication.run(BilibiliMonitorApplication.class, args);
    }

}
