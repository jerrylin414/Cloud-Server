package com.lzy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @auther jerry
 * @date 3/9/2024 5:24 PM
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TaskInfoDto {

    /**
     * 是否完成上传（是否已经合并分片）
     */
    private boolean finished;

    /**
     * 文件地址
     */
    private String path;

    /**
     * 上传记录
     */
    private TaskRecordDto taskRecord;
}
