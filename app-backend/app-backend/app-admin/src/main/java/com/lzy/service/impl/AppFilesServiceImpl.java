package com.lzy.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.lzy.bo.InitTaskParam;
import com.lzy.common.CommonResult;
import com.lzy.common.ResultCode;
import com.lzy.common.ViewContentTypeEnum;
import com.lzy.dao.AppFilesMapper;
import com.lzy.entity.AppFiles;
import com.lzy.entity.AppFilesExample;
import com.lzy.oss.model.OssFile;
import com.lzy.oss.template.MinioTemplate;
import com.lzy.service.AppFilesService;
import com.lzy.util.FileUtil;
import com.lzy.util.MinioUtil;
import com.lzy.vo.AppFilesVo;
import io.minio.GetObjectResponse;
import io.minio.StatObjectResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class AppFilesServiceImpl implements AppFilesService {
    private static Logger logger = LogManager.getLogger(AppFilesServiceImpl.class);

    private static final Integer BUFFER_SIZE = 1024 * 64; // 64KB

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Autowired
    private AppFilesMapper appFilesMapper;

    @Autowired
    private MinioTemplate minioTemplate;

    @Resource
    private MinioUtil minioUtil;

    @Override
    public CommonResult upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return CommonResult.failed();
        }
        String path = "";
        String originalFilename = file.getOriginalFilename();
        String ext = "." + originalFilename.split("\\.")[1]; //1.png

        lock.writeLock().lock();
        try {
            path = getPath(ext, path, file);
        } catch (IOException e) {
            path = "error";//json report error
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
        return CommonResult.success(path);
    }

    @Override
    public CommonResult minioUpload(MultipartFile file, Long folderId, Long userId) {
        if (file == null || file.isEmpty()) {
            return CommonResult.failed("400 file is null");
        }
        String size = FileUtil.formatFileSize(file.getSize());
        String originalName = file.getOriginalFilename();
        String suffix = FileUtil.getFileExtension(originalName);
        String name = originalName.substring(0, originalName.length() - suffix.length() - 1);
        String type = ViewContentTypeEnum.getContentType(suffix);
        OssFile ossFile = minioTemplate.upLoadFile("", name, file);
        Boolean flag = add2Db(ossFile, originalName, folderId, userId, type, size);
        if (flag) {
            return CommonResult.success(ossFile);
        }
        return CommonResult.failed();
    }

    @Override
    public CommonResult showAllFiles(Long userId) {
        if (ObjUtil.isNull(userId)) {
            return CommonResult.failed();
        }
        AppFilesExample example = new AppFilesExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<AppFiles> appFiles = appFilesMapper.selectByExample(example);
        return CommonResult.success(appFiles);
    }

    @Override
    public CommonResult showFilesByName(Long userId, String fileName) {
        if (ObjUtil.isNull(userId) || ObjUtil.isNull(fileName)) {
            return CommonResult.failed();
        }
        AppFilesExample exa = new AppFilesExample();
        exa.createCriteria().andUserIdEqualTo(userId).andFileNameEqualTo(fileName);
        List<AppFiles> list = appFilesMapper.selectByExample(exa);
        return CommonResult.success(list);
    }

    @Override
    @Transactional
    public CommonResult batchToBin(List<Long> idList) {
        try {
            appFilesMapper.batchToBin(idList);
        } catch (Exception e) {
            logger.error("批量入回收站失败，数据回滚", e);
            e.printStackTrace();
        }
        return CommonResult.success(ResultCode.SUCCESS);
    }

    @Override
    public void downloadMinio(AppFiles vo, HttpServletResponse response) {
        if (ObjUtil.isEmpty(vo.getId()) || ObjUtil.isEmpty(vo.getFilePath())) {
            return;
        }
        try {
            String pathUrl = parseUrl(vo.getFilePath());
            minioTemplate.downloadFile(response, vo.getOriginName(), pathUrl);
        } catch (Exception e) {
            logger.error("================== downloadFile failed ==================", e);
            e.printStackTrace();
        }
    }

    private String parseUrl(String url) {
        if (url == null || !url.startsWith("http://") && !url.startsWith("https://")) {
            throw new IllegalArgumentException("Invalid URL provided");
        }

        // 找到"image-bucket"在URL中最后出现的位置
//        int index = url.lastIndexOf("/image-bucket/");
        int index = url.lastIndexOf("/share/");
        if (index == -1) {
            throw new IllegalArgumentException("'image-bucket' not found in the URL");
        }

        // 获取"image-bucket"后面的部分（包括后面的"/"）
//        return url.substring(index + "/image-bucket/".length());
        return url.substring(index + "/share/".length());
    }

    private Boolean add2Db(OssFile ossFile, String originalName, Long folderId, Long userId, String type, String size) {
        if (ObjUtil.isNotNull(ossFile)) {
            String filePath = ossFile.getFilePath(); //------//20240710/test_1720596876541.png
            String resPath = Optional.ofNullable(filePath)
                    .map(s -> s.length() > 1 ? s.substring(1) : null).orElse(null);
            String headerPath = ossFile.getDomain();
            if (ObjUtil.isNotNull(resPath) && ObjUtil.isNotNull(headerPath)) {
                AppFiles appFiles = new AppFiles();
                String[] parts = filePath.split("/");
                String result = parts[parts.length - 1];
                appFiles.setFileData(size);
                appFiles.setFileName(result);
                appFiles.setOriginName(originalName);
                appFiles.setFolderId(folderId);
                appFiles.setFileType("---");
                appFiles.setFilePath(headerPath + resPath);
                appFiles.setUserId(userId);
                appFiles.setCreateTime(new Date());
                appFiles.setIsBin("N");
                appFiles.setIsDeleted("N");
                appFiles.setFileType(type);
                appFilesMapper.insert(appFiles);
                return true;
            }
        }
        return false;
    }

    private String getPath(String ext, String path, MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + ext;
        ApplicationHome home = new ApplicationHome(this.getClass());
        String pre = home.getDir().getParentFile().getParentFile().getAbsolutePath() + "\\src\\main\\resources\\images\\";
        path = pre + fileName;
        file.transferTo(new File(path));
        return path;
    }

    @Override
    public ResponseEntity<byte[]> downloadMultipartFile(Long id, String range, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // redis 缓存当前文件信息，避免分片下载时频繁查库
        AppFiles file = appFilesMapper.selectByPrimaryKey(id);
//        InitTaskParam redisFile = (InitTaskParam)redisUtil.get(String.valueOf(id));
//        if (redisFile == null) {
//            AppFiles dbFile = appFilesMapper.selectByPrimaryKey(id);
//            if (dbFile == null) {
//                return null;
//            } else {
//                file = dbFile;
//                redisUtil.set(String.valueOf(id), file, 1, TimeUnit.DAYS);
//            }
//        } else {
//            file = redisFile;
//        }

        String fileName = file.getFileName();
        String objectPath = getObject(file.getFilePath());
        logger.info("下载文件的 object <{}>", objectPath);
        // 获取 bucket 桶中的文件元信息，获取不到会抛出异常
        StatObjectResponse objectResponse = minioUtil.statObject(objectPath);
        long startByte = 0; // 开始下载位置
        long fileSize = objectResponse.size();
        long endByte = fileSize - 1; // 结束下载位置
        logger.info("文件总长度：{}，当前 range：{}", fileSize, range);

        BufferedOutputStream os = null; // buffer 写入流
        GetObjectResponse stream = null; // minio 文件流

        // 存在 range，需要根据前端下载长度进行下载，即分段下载
        // 例如：range=bytes=0-52428800
        if (range != null && range.contains("bytes=") && range.contains("-")) {
            range = range.substring(range.lastIndexOf("=") + 1).trim(); // 0-52428800
            String[] ranges = range.split("-");
            // 判断range的类型
            if (ranges.length == 1) {
                // 类型一：bytes=-2343 后端转换为 0-2343
                if (range.startsWith("-")) endByte = Long.parseLong(ranges[0]);
                // 类型二：bytes=2343- 后端转换为 2343-最后
                if (range.endsWith("-")) startByte = Long.parseLong(ranges[0]);
            } else if (ranges.length == 2) { // 类型三：bytes=22-2343
                startByte = Long.parseLong(ranges[0]);
                endByte = Long.parseLong(ranges[1]);
            }
        }

        // 要下载的长度
        // 确保返回的 contentLength 不会超过文件的实际剩余大小
        long contentLength = Math.min(endByte - startByte + 1, fileSize - startByte);
        // 文件类型
        String contentType = request.getServletContext().getMimeType(fileName);

        // 解决下载文件时文件名乱码问题
        byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        fileName = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.ISO_8859_1);

        // 响应头设置---------------------------------------------------------------------------------------------
        // 断点续传，获取部分字节内容：
        response.setHeader("Accept-Ranges", "bytes");
        // http状态码要为206：表示获取部分内容,SC_PARTIAL_CONTENT,若部分浏览器不支持，改成 SC_OK
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setContentType(contentType);
        response.setHeader("Last-Modified", objectResponse.lastModified().toString());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(contentLength));
        // Content-Range，格式为：[要下载的开始位置]-[结束位置]/[文件总大小]
        response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + objectResponse.size());
        response.setHeader("ETag", "\"".concat(objectResponse.etag()).concat("\""));
        response.setContentType("application/octet-stream;charset=UTF-8");

        try {
            // 获取文件流
            stream = minioUtil.getObject(objectResponse.object(), startByte, contentLength);
            os = new BufferedOutputStream(response.getOutputStream());
            // 将读取的文件写入到 OutputStream
            byte[] bytes = new byte[BUFFER_SIZE];
            long bytesWritten = 0;
            int bytesRead = -1;
            while ((bytesRead = stream.read(bytes)) != -1) {
                if (bytesWritten + bytesRead >= contentLength) {
                    os.write(bytes, 0, (int) (contentLength - bytesWritten));
                    break;
                } else {
                    os.write(bytes, 0, bytesRead);
                    bytesWritten += bytesRead;
                }
            }
            os.flush();
            response.flushBuffer();
            // 返回对应http状态
            return new ResponseEntity<>(bytes, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) os.close();
            if (stream != null) stream.close();
        }
        return null;
    }

    private String getObject(String fileName) {
        //http://127.0.0.1:9000/share/2024-10-29/38a6343a-15ca-4b7b-8670-346118f838e3.zip
        int shareIndex = fileName.indexOf("share/");
        int nextSlashIndex = fileName.indexOf("/", shareIndex + "share".length());
        String result = fileName.substring(nextSlashIndex);
        return result;
    }
}
