package com.lzy.util;

import com.lzy.config.CustomMinioClient;
import com.lzy.config.MinioProperties;
import io.minio.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
public class MinioUtil {
    private CustomMinioClient customMinioClient;

    @Resource
    private MinioProperties minioProperties;

    // spring自动注入会失败
    @PostConstruct
    public void init() {
        MinioAsyncClient minioClient = MinioAsyncClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        customMinioClient = new CustomMinioClient(minioClient);
    }

    @SneakyThrows
    public StatObjectResponse statObject(String object) {
        return customMinioClient.statObject(StatObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(object)
                .build())
                .get();
    }

    @SneakyThrows
    public GetObjectResponse getObject(String object, Long offset, Long contentLength) {
        return customMinioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(object)
                .offset(offset)
                .length(contentLength)
                .build())
                .get();
    }
}
