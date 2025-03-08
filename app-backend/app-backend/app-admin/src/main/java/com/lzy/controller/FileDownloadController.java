package com.lzy.controller;

import com.lzy.common.CommonResult;
import com.lzy.entity.AppFiles;
import com.lzy.service.impl.AppFilesServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther jerry
 * @date 18/7/2024 4:29 PM
 */
@Controller
@RequestMapping("/files")
public class FileDownloadController {
    private static Logger logger = LogManager.getLogger(FileDownloadController.class);

    @Autowired
    private AppFilesServiceImpl appFilesService;

    @RequestMapping(value = "/downloadMinio", method = RequestMethod.GET)
    public void downloadMinio(@RequestParam Long id, @RequestParam String originName, @RequestParam String filePath, HttpServletResponse response) {
        try {
            AppFiles appFiles = new AppFiles();
            appFiles.setId(id);
            appFiles.setOriginName(originName);
            appFiles.setFilePath(filePath);
            appFilesService.downloadMinio(appFiles, response);
        } catch (Exception e) {
            logger.error("====== download faied ======", e);
            e.printStackTrace();
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadMultipartFile(@RequestParam Long id, @RequestParam String range, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("通过 <{}> 开始分片下载", id);
        return appFilesService.downloadMultipartFile(id, range, request, response);
    }
}
