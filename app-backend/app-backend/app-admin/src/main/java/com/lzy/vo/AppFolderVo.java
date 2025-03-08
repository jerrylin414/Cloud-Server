package com.lzy.vo;

import com.lzy.entity.AppFiles;
import com.lzy.entity.AppFolder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @auther jerry
 * @date 10/7/2024 3:09 PM
 */
@Data
public class AppFolderVo extends AppFolder {
    private List<AppFolderVo> childrenFolders;
    private Long targetId;
    private String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppFolderVo)) return false;
        if (!super.equals(o)) return false;
        AppFolderVo that = (AppFolderVo) o;
        return Objects.equals(getChildrenFolders(), that.getChildrenFolders());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getChildrenFolders());
    }
}
