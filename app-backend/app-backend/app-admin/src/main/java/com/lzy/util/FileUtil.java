package com.lzy.util;

import cn.hutool.core.lang.Assert;

import java.io.File;

/**
 * @auther jerry
 * @date 15/7/2024 11:02 AM
 */
public class FileUtil {
    public static String getFileExtension(String fullName) {
        Assert.notNull(fullName, "minio file fullName is null.");
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }

}
