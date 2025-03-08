package com.lzy.service;

import com.lzy.bo.InitTaskParam;
import com.lzy.common.CommonResult;
import com.lzy.dto.TaskInfoDto;
import com.lzy.entity.SysUploadTask;

import java.util.Map;

/**
 * @auther jerry
 * @date 3/9/2024 5:38 PM
 */
public interface SysUploadTaskService {

    /**
     * 查询是否上传过，若存在，返回TaskInfoDTO
     * @param identifier
     * @return
     */
    TaskInfoDto getTaskInfo (String identifier);

    /**
     * 根据md5标识获取分片上传任务
     * @param identifier
     * @return
     */
    SysUploadTask getByIdentifier (String identifier);

    /**
     * 初始化一个任务
     */
    TaskInfoDto initTask (InitTaskParam param);

    /**
     * 获取文件地址
     * @param bucket
     * @param objectKey
     * @return
     */
    String getPath (String bucket, String objectKey);

    /**
     * 生成预签名上传url
     * @param bucket 桶名
     * @param objectKey 对象的key
     * @param params 额外的参数
     * @return
     */
    String genPreSignUploadUrl (String bucket, String objectKey, Map<String, String> params);

    /**
     * 合并分片
     * @param identifier
     */
    CommonResult merge (String identifier);
}
