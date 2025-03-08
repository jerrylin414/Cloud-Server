package com.lzy.config;


import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
@ConditionalOnProperty(value = "oss.name", havingValue = "minio")
public class MinioConfiguration {

    @Resource
    private MinioProperties ossProperties;

    @Bean
    @SneakyThrows
    public MinioClient minioClient() {
        System.out.println("oss" + ossProperties);
        return MinioClient.builder()
                .endpoint(ossProperties.getEndpoint())
                .credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey())
                .build();
    }
}
