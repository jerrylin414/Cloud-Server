package com.lzy.service;

import com.lzy.common.CommonResult;
import com.lzy.entity.AppFolder;
import com.lzy.vo.AppFilesVo;
import com.lzy.vo.AppFolderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jerry
 * @date 2024-07-10 11:49
 */

public interface AppFolderService {
    CommonResult addFolder(String folderName,Long userId,Long partendId);

    CommonResult editFolder(Long folderId,String newName);

    CommonResult deleteFolder(Long id);//回收站里面的删除

    CommonResult deleteFolder2Bin(AppFilesVo appFilesVo);

    CommonResult firstShow(Long userId,Integer pageNum,Integer pageSize);

    CommonResult getFilesAndFolders(Long userId,Long folderId);

    CommonResult batchToBin(@Param("idList") List<Long> idList);

    CommonResult moveFolder(AppFolderVo vo);
}
