package com.lzy.vo;

import com.lzy.entity.AppFiles;
import lombok.Data;

@Data
public class AppFilesVo extends AppFiles {
    private Long userId;
    private Long folderId;
    private Long fileId;
    private Long fileOrFolderId;
    private String fileType;
    private String name;
    private String shareTime;
}