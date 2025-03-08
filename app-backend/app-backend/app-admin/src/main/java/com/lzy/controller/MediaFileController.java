package com.lzy.controller;

import com.lzy.bo.InitTaskParam;
import com.lzy.common.CommonResult;
import com.lzy.dto.TaskInfoDto;
import com.lzy.entity.SysUploadTask;
import com.lzy.service.SysUploadTaskService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther jerry
 * @date 4/9/2024 9:21 AM
 */
@RestController
@RequestMapping("/v1/minio/tasks")
public class MediaFileController {
    @Resource
    private SysUploadTaskService sysUploadTaskService;

    /**
     * 获取上传进度
     * @param identifier 文件md5
     * @return
     */
    @GetMapping("/{identifier}")
    public CommonResult<TaskInfoDto> taskInfo (@PathVariable("identifier") String identifier) {
        return CommonResult.ok(sysUploadTaskService.getTaskInfo(identifier));
    }

    /**
     * 创建一个上传任务
     * @return
     */
    @PostMapping
    public CommonResult<TaskInfoDto> initTask (@Valid @RequestBody InitTaskParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return CommonResult.failed(bindingResult.getFieldError().getDefaultMessage());
        }
        return CommonResult.ok(sysUploadTaskService.initTask(param));
    }

    /**
     * 获取每个分片的预签名上传地址
     * @param identifier
     * @param partNumber
     * @return
     */
    @GetMapping("/{identifier}/{partNumber}")
    public CommonResult preSignUploadUrl (@PathVariable("identifier") String identifier, @PathVariable("partNumber") Integer partNumber) {
        SysUploadTask task = sysUploadTaskService.getByIdentifier(identifier);
        if (task == null) {
            return CommonResult.failed("分片任务不存在");
        }
        Map<String, String> params = new HashMap<>();
        params.put("partNumber", partNumber.toString());
        params.put("uploadId", task.getUploadId());
        return CommonResult.ok(sysUploadTaskService.genPreSignUploadUrl(task.getBucketName(), task.getObjectKey(), params));
    }

    /**
     * 合并分片
     * @param identifier
     * @return
     */
    @PostMapping("/merge/{identifier}")
    public CommonResult merge (@PathVariable("identifier") String identifier) {
        return sysUploadTaskService.merge(identifier);
    }
}
