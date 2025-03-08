package com.lzy.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author jerry
 * @date 2024-07-10 11:47
 */
@Configuration
@MapperScan("com.lzy.dao")
public class MyBatisConfig {
}
