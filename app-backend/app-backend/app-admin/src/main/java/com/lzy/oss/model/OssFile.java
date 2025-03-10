package com.lzy.oss.model;

import lombok.Data;
import java.util.Date;

/**
 * OssFile
 *
 * @author meng
 */
@Data
public class OssFile {
    /**
     * 文件地址
     */
    private String filePath;
    /**
     * 域名地址
     */
    private String domain;
    /**
     * 文件名
     */
    private String name;
    /**
     * 原始文件名
     */
    private String originalName;
    /**
     * 文件hash值
     */
    public String hash;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 文件上传时间
     */
    private Date putTime;
    /**
     * 文件contentType
     */
    private String contentType;
}
