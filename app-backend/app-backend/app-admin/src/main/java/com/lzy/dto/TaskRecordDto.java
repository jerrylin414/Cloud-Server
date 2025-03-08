package com.lzy.dto;

import cn.hutool.core.bean.BeanUtil;
import com.amazonaws.services.s3.model.PartSummary;
import com.lzy.entity.SysUploadTask;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @auther jerry
 * @date 3/9/2024 5:24 PM
 */
@Data
@ToString
@Accessors(chain = true)
public class TaskRecordDto extends SysUploadTask{
    /**
     * 已上传完的分片
     */
    private List<PartSummary> exitPartList;

    public static TaskRecordDto convertFromEntity(SysUploadTask task){
        TaskRecordDto dto = new TaskRecordDto();
        BeanUtil.copyProperties(task,dto);
        return dto;
    }
}
