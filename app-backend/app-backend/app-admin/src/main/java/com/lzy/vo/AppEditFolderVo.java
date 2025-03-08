package com.lzy.vo;

import com.lzy.entity.AppFolder;
import lombok.Data;

@Data
public class AppEditFolderVo extends AppFolder {
    private String newName;
}
