package com.lzy.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.lzy.bo.InitTaskParam;
import com.lzy.common.CommonResult;
import com.lzy.common.CommonStatus;
import com.lzy.config.MinioProperties;
import com.lzy.dao.AppFilesMapper;
import com.lzy.dao.SysUploadTaskMapper;
import com.lzy.dto.TaskInfoDto;
import com.lzy.dto.TaskRecordDto;
import com.lzy.entity.AppFiles;
import com.lzy.entity.AppFilesExample;
import com.lzy.entity.SysUploadTask;
import com.lzy.entity.SysUploadTaskExample;
import com.lzy.service.SysUploadTaskService;
import com.lzy.util.MybatisBatchUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @auther jerry
 * @date 3/9/2024 5:43 PM
 */
@Service
public class SysUploadTaskServiceImpl implements SysUploadTaskService {

    // 预签名url过期时间(ms)
    private static final Long PRE_SIGN_URL_EXPIRE = 60 * 10 * 1000L;
    private static Logger log = LogManager.getLogger(SysUploadTaskServiceImpl.class);

    @Resource
    private AmazonS3 amazonS3;

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private SysUploadTaskMapper sysUploadTaskMapper;

    @Autowired
    private AppFilesMapper appFilesMapper;

    @Override
    public TaskInfoDto getTaskInfo(String identifier) {
        SysUploadTask task = getByIdentifier(identifier);
        if (task == null) {
            return null;
        }
        TaskInfoDto result = new TaskInfoDto().setFinished(true).setTaskRecord(TaskRecordDto.convertFromEntity(task)).setPath(getPath(task.getBucketName(), task.getObjectKey()));

        boolean doesObjectExist = amazonS3.doesObjectExist(task.getBucketName(), task.getObjectKey());
        if (!doesObjectExist) {
            // 未上传完，返回已上传的分片
            ListPartsRequest listPartsRequest = new ListPartsRequest(task.getBucketName(), task.getObjectKey(), task.getUploadId());
            PartListing partListing = amazonS3.listParts(listPartsRequest);
            result.setFinished(false).getTaskRecord().setExitPartList(partListing.getParts());
        }
        return result;
    }

    @Override
    public SysUploadTask getByIdentifier(String identifier) {
        SysUploadTaskExample example = new SysUploadTaskExample();
        example.createCriteria().andFileIdentifierEqualTo(identifier);
        List<SysUploadTask> sysUploadTasks = sysUploadTaskMapper.selectByExample(example);
        SysUploadTask task = Optional.ofNullable(sysUploadTasks)
                .filter(s -> !s.isEmpty())
                .map(s -> s.get(0))
                .orElse(null);
        return task;
    }

    @Override
    public TaskInfoDto initTask(InitTaskParam param) {

        Date currentDate = new Date();
        String bucketName = minioProperties.getBucketName();
        String fileName = param.getFileName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String key = StrUtil.format("{}/{}.{}", DateUtil.format(currentDate, "YYYY-MM-dd"), IdUtil.randomUUID(), suffix);
        String contentType = MediaTypeFactory.getMediaType(key).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        InitiateMultipartUploadResult initiateMultipartUploadResult = amazonS3
                .initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, key).withObjectMetadata(objectMetadata));
        String uploadId = initiateMultipartUploadResult.getUploadId();

        SysUploadTask task = new SysUploadTask();
        int chunkNum = (int) Math.ceil(param.getTotalSize() * 1.0 / param.getChunkSize());
        // 获得全局唯一主键
        Long id = System.currentTimeMillis();
        task.setBucketName(minioProperties.getBucketName())
                .setChunkNum(chunkNum)
                .setChunkSize(param.getChunkSize())
                .setTotalSize(param.getTotalSize())
                .setFileIdentifier(param.getIdentifier())
                .setFileName(fileName)
                .setObjectKey(key)
                .setUploadId(uploadId)
                .setId(id);
        sysUploadTaskMapper.insert(task);
        TaskRecordDto recordDto = TaskRecordDto.convertFromEntity(task);
        TaskInfoDto dto = new TaskInfoDto();
        dto.setFinished(false).setTaskRecord(recordDto).setPath(getPath(bucketName, key));

        //add to app_files db
        AppFiles appFiles = new AppFiles();
        Long totalSize = param.getTotalSize();//B
        Long sizeKb = totalSize / 1024 / 1024;
        appFiles.setUserId(param.getUserId());
        appFiles.setFolderId(param.getFolderId());
        appFiles.setOriginName(param.getFileName());
        appFiles.setFileName(param.getFileName());
        appFiles.setFileType(param.getFileName().substring(param.getFileName().length() - 4));
        appFiles.setFilePath(getPath(bucketName, key));
        appFiles.setFileData(sizeKb + "KB");
        appFiles.setFileIdentifier(param.getIdentifier());
        appFiles.setIsBin(CommonStatus.NO_BIN);
        appFiles.setIsDeleted(CommonStatus.NO_DELETED);
        appFiles.setIsFinished(CommonStatus.NO_FINISHED);
        appFiles.setFileSize(totalSize);
        appFiles.setCreateTime(new Date());
        try {
            appFilesMapper.insert(appFiles);
            log.info("============ INSERT APP_FILES SUCCESS ============", appFiles.getId());
        } catch (Exception e) {
            log.error("============ FAILED !!! INSERT APP_FILES FAILED ============", appFiles.getId());
            e.printStackTrace();
        }
        return dto;
    }

    @Override
    public String getPath(String bucket, String objectKey) {
        return StrUtil.format("{}/{}/{}", minioProperties.getEndpoint(), bucket, objectKey);
    }

    @Override
    public String genPreSignUploadUrl(String bucket, String objectKey, Map<String, String> params) {
        Date currentDate = new Date();
        Date expireDate = DateUtil.offsetMillisecond(currentDate, PRE_SIGN_URL_EXPIRE.intValue());
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, objectKey)
                .withExpiration(expireDate).withMethod(HttpMethod.PUT);
        if (params != null) {
            params.forEach((key, val) -> request.addRequestParameter(key, val));
        }
        URL preSignedUrl = amazonS3.generatePresignedUrl(request);
        return preSignedUrl.toString();
    }

    @Override
    public CommonResult merge(String identifier) {
        SysUploadTask task = getByIdentifier(identifier);
        if (task == null) {
            throw new RuntimeException("分片任务不存");
        }
        ListPartsRequest listPartsRequest = new ListPartsRequest(task.getBucketName(), task.getObjectKey(), task.getUploadId());
        PartListing partListing = amazonS3.listParts(listPartsRequest);
        List<PartSummary> parts = partListing.getParts();
        if (!task.getChunkNum().equals(parts.size())) {
            // 已上传分块数量与记录中的数量不对应，不能合并分块
            throw new RuntimeException("分片缺失，请重新上传");
        }
        CompleteMultipartUploadResult result = null;
        try {
            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest()
                    .withUploadId(task.getUploadId())
                    .withKey(task.getObjectKey())
                    .withBucketName(task.getBucketName())
                    .withPartETags(parts.stream().map(partSummary -> new PartETag(partSummary.getPartNumber(), partSummary.getETag())).collect(Collectors.toList()));
            result = amazonS3.completeMultipartUpload(completeMultipartUploadRequest);
        } catch (SdkClientException e) {
            log.error("==========Merge Error========== Failed!!!", result + "identifier = " + identifier);
            e.printStackTrace();
        }

        try {
            AppFilesExample example = new AppFilesExample();
            example.createCriteria().andFileIdentifierEqualTo(identifier);
            List<AppFiles> filesList = appFilesMapper.selectByExample(example);
            if (filesList != null && filesList.size() > 0) {
                for (int i = 0; i < filesList.size(); i++) {
                    AppFiles files = filesList.get(i);
                    if (!CommonStatus.IS_FINISHED.equals(files.getIsFinished())){
                        files.setIsFinished(CommonStatus.IS_FINISHED);
                        appFilesMapper.updateByPrimaryKey(files);
                    }
                }
            }
        } catch (Exception e) {
            log.error("==========Update Error========== Failed!!!", identifier);
            e.printStackTrace();
        }

        return CommonResult.success(result);
    }
}
