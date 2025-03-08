package com.lzy.oss.template;

// 修改为自己项目的类路径
import com.lzy.oss.model.OssFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * OssTemplate抽象API
 *
 * @author meng
 */
public interface OssTemplate {

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    boolean bucketExists(String bucketName);


    /**
     * 获取文件信息
     *
     * @param fileName 存储桶文件名称
     * @return InputStream
     */
    OssFile getOssInfo(String fileName);

    /**
     * 上传文件
     *
     * @param folderName 上传的文件夹名称
     * @param fileName   上传文件名
     * @param file       上传文件类
     * @return BladeFile
     */
    OssFile upLoadFile(String folderName, String fileName, MultipartFile file);

    /**
     * 上传文件
     *
     * @param folderName 上传的文件夹名称
     * @param fileName   存储桶对象名称
     * @param suffix     文件后缀名
     * @param stream     文件流
     * @return BladeFile
     */
    OssFile upLoadFile(String folderName, String fileName, String suffix, InputStream stream);

    /**
     * 删除文件
     *
     * @param fileName 存储桶对象名称
     */
    boolean removeFile(String fileName);

    /**
     * 批量删除文件
     *
     * @param fileNames 存储桶对象名称集合
     */
    boolean removeFiles(List<String> fileNames);

    /**
     * @Description: 下载文件
     * @Param response: 响应
     * @Param fileName: 文件名
     * @Param filePath: 文件路径
     * @return: void
     */
    void downloadFile(HttpServletResponse response, String fileName, String filePath);
}
