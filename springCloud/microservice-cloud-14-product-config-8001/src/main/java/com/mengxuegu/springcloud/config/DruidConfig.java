package com.mengxuegu.springcloud.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * create by biji.zhao on 2020/10/18
 *
 * 解决数据源配置更新 不生效的问题  需要定义相应数据源类来热更新配置
 */
@Configuration
public class DruidConfig {

    @RefreshScope
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid() {
        return new DruidDataSource();
    }
}
