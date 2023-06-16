package com.monitor.bilibili_monitor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Slf4j
public class TestController {
    @GetMapping("/")
    public void test() {
        log.info("连接成功");
    }
}
