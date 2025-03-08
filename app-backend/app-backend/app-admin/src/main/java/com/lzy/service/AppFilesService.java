package com.lzy.service;


import com.lzy.common.CommonResult;
import com.lzy.entity.AppFiles;
import com.lzy.vo.AppFilesVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AppFilesService {
    CommonResult upload(MultipartFile file);//

    CommonResult minioUpload(MultipartFile file,Long folderId,Long userId);

    CommonResult showAllFiles(Long userId);

    CommonResult showFilesByName(Long userId,String fileName);

    CommonResult batchToBin(@Param("idList") List<Long> idList);

    void downloadMinio(AppFiles vo, HttpServletResponse response);

    ResponseEntity<byte[]> downloadMultipartFile(Long id, String range, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
