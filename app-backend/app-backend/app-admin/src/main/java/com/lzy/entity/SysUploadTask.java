package com.lzy.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
public class SysUploadTask implements Serializable {
    private Long id;
    private String uploadId;
    private String fileIdentifier;
    private String fileName;
    private String bucketName;
    private String objectKey;
    private Long totalSize;
    private Long chunkSize;
    private Integer chunkNum;
    private static final long serialVersionUID = 1L;
}