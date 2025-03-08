package com.lzy.controller;

import com.lzy.common.CommonResult;
import com.lzy.entity.AppFiles;
import com.lzy.service.impl.AppFilesServiceImpl;
import com.lzy.vo.AppFilesVo;
import com.lzy.vo.TestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class AppFilesController {
    @Autowired
    private AppFilesServiceImpl appFilesService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public CommonResult uploadOrigin(MultipartFile file) {
        return appFilesService.upload(file);
    }

    @RequestMapping(value = "/uploadMinio", method = RequestMethod.POST)
    public CommonResult uploadMinio(@RequestParam(value = "file", required = false) MultipartFile file
            , @RequestParam(value = "folderId", defaultValue = "0") Long folderId,
                                    @RequestParam Long userId) {
        return appFilesService.minioUpload(file, folderId, userId);
    }



}
