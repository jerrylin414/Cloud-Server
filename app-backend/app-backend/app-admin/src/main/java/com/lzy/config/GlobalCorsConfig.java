package com.lzy.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer  {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 或者使用 allowedOriginPatterns 来支持模式匹配
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                .allowCredentials(true)
                .allowedHeaders("*")
                .maxAge(18000);
    }
//    private static final long MAX_AGE = 24 * 60 * 60;
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("*"); // 1 设置访问源地址，或者http://localhost:7060
//        corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
//        corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法，或设置为"GET", "POST", "DELETE", "PUT"
//        corsConfiguration.setMaxAge(MAX_AGE);
//        source.registerCorsConfiguration("/**", corsConfiguration); // 4 对接口配置跨域设置
//        return new CorsFilter(source);
//    }
}
