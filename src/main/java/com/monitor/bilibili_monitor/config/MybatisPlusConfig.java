package com.monitor.bilibili_monitor.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: mybatis-plus配置
 */
@Configuration
@MapperScan(basePackages = {"com.monitor.bilibili_monitor.mapper"})
public class MybatisPlusConfig {

    /**
     * mybatis-plus插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        List list = new ArrayList<InnerInterceptor>();
        // 分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(500L);
        list.add(paginationInnerInterceptor);
        // sql性能规范
        list.add(new IllegalSQLInnerInterceptor());
        // 防止全表更新与删除
        list.add(new BlockAttackInnerInterceptor());
        interceptor.setInterceptors(list);
        return interceptor;
    }

}
