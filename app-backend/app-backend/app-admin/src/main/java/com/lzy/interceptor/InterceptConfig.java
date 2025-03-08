package com.lzy.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @auther jerry
 * @date 11/7/2024 2:36 PM
 */
@Configuration
public class InterceptConfig implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        //添加拦截器
//        registry.addInterceptor(jwtInterceptor)
//                //拦截所有请求
//                .addPathPatterns("/**")
//                //放行的路径
//                .excludePathPatterns("/user/**").excludePathPatterns("/share/getCode");
//    }
}
