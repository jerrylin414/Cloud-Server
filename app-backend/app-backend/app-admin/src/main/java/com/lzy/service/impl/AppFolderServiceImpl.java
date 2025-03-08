package com.lzy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.lzy.common.CommonResult;
import com.lzy.common.CommonStatus;
import com.lzy.common.ResultCode;
import com.lzy.dao.AppFilesMapper;
import com.lzy.dao.AppFolderMapper;
import com.lzy.entity.AppFiles;
import com.lzy.entity.AppFilesExample;
import com.lzy.entity.AppFolder;
import com.lzy.entity.AppFolderExample;
import com.lzy.exception.ServiceException;
import com.lzy.exception.ServiceExceptionHandler;
import com.lzy.service.AppFolderService;
import com.lzy.vo.AppFilesVo;
import com.lzy.vo.AppFolderVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AppFolderServiceImpl implements AppFolderService {
    private static Logger logger = LogManager.getLogger(AppFolderServiceImpl.class);

    @Autowired
    private AppFolderMapper appFolderMapper;

    @Autowired
    private AppFilesMapper appFilesMapper;

    @Override
    public CommonResult addFolder(String folderName, Long userId, Long partendId) {
        if (StrUtil.isNotBlank(folderName) && ObjUtil.isNotNull(userId)) {
            AppFolder appFolder = new AppFolder();
            appFolder.setName(folderName);
            appFolder.setUserId(userId);
            appFolder.setParentId(partendId);
            appFolder.setIsDeleted("N");
            appFolder.setIsBin("N");
            appFolder.setCreateTime(new Date());
            appFolderMapper.insert(appFolder);
            return CommonResult.success(appFolderMapper.selectByPrimaryKey(appFolder.getId()));
        }
        return CommonResult.failed();
    }

    @Override
    public CommonResult editFolder(Long folderId, String newName) {
        if (ObjUtil.isNull(folderId) || ObjUtil.isEmpty(newName)){
            logger.error("------error-----", folderId);
            return CommonResult.failed();
        }
        try{
            AppFolderExample example = new AppFolderExample();
            example.createCriteria().andIdEqualTo(folderId);
            List<AppFolder> folderList = appFolderMapper.selectByExample(example);
            if (folderList != null && folderList.size() > 0){
                AppFolder folder = folderList.get(0);
                folder.setName(newName);
                appFolderMapper.updateByPrimaryKey(folder);
                logger.info("------change folder name success-----",folderId);
            }else {
                return CommonResult.failed();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.success(ResultCode.SUCCESS);
    }

    @Override
    public CommonResult deleteFolder(Long id) {
        if (ObjUtil.isNotNull(id)) {
            AppFolder appFolder = appFolderMapper.selectByPrimaryKey(id);
            if (CommonStatus.IS_BIN.equals(appFolder.getIsBin())) {
                appFolder.setIsDeleted(CommonStatus.IS_DELETED);
                int udpateKey = appFolderMapper.updateByPrimaryKey(appFolder);
                if (udpateKey > 0) {
                    appFolderMapper.deleteByPrimaryKey(id);
                } else {
                    return CommonResult.failed();
                }
                return CommonResult.success(id, "delete_success");
            }
        }
        return CommonResult.failed();
    }

    @Override
    @Transactional
    public CommonResult deleteFolder2Bin(AppFilesVo appFilesVo) {
        //判断前端传来文件类型
        if (ObjUtil.isNull(appFilesVo)) {
            return CommonResult.failed();
        }
        Long id = appFilesVo.getFileOrFolderId();
        Long userId = appFilesVo.getUserId();

        if (ObjUtil.isNull(id) || ObjUtil.isNull(userId)) {
            return CommonResult.failed();
        }
        try {
            if (CommonStatus.IS_FOLDER.equals(appFilesVo.getFileType())) {
                AppFolderExample fe = new AppFolderExample();
                fe.createCriteria().andIdEqualTo(id).andUserIdEqualTo(userId);
                List<AppFolder> list = appFolderMapper.selectByExample(fe);
                AppFolder folder = Optional.ofNullable(list).filter(s -> !s.isEmpty()).map(s -> s.get(0)).orElse(null);

                //所有文件夹以及子文件夹ID
                logger.info("======开始批量操作目录文件======");
                List<Long> folderIdList = Optional.ofNullable(folder).map(s -> getAllChildFolderIds(s.getId())).orElse(null);
                List<Long> filesIdList = appFilesMapper.idListByFolderId(folderIdList);
                //批量逻辑删除所有文件夹和文件
                appFolderMapper.batchToBin(folderIdList);
                appFilesMapper.batchToBin(filesIdList);
                logger.info("======删除多级目录文件完成======");
            } else {
                AppFilesExample fs = new AppFilesExample();
                fs.createCriteria().andIdEqualTo(id).andUserIdEqualTo(userId);
                List<AppFiles> files = appFilesMapper.selectByExample(fs);
                AppFiles file = Optional.ofNullable(files).filter(s -> !s.isEmpty()).map(s -> s.get(0)).orElse(null);
                file.setIsBin(CommonStatus.IS_BIN);
                appFilesMapper.updateByPrimaryKey(file);
            }
        } catch (Exception e) {
            logger.error("======删除多级目录文件失败======", e);
            e.printStackTrace();
        }
        return CommonResult.success(ResultCode.SUCCESS);
    }

    @Override
    public CommonResult firstShow(Long userId, Integer pageNum, Integer pageSize) {
        AppFolderExample exa = new AppFolderExample();
        exa.createCriteria().andUserIdEqualTo(userId).andParentIdEqualTo(0l).andIsBinEqualTo(CommonStatus.NO_BIN).andIsDeletedEqualTo(CommonStatus.NO_DELETED);
        List<AppFolder> folderList = appFolderMapper.selectByExample(exa);

        AppFilesExample example = new AppFilesExample();
        example.createCriteria().andUserIdEqualTo(userId).andFolderIdEqualTo(0l).andIsBinEqualTo(CommonStatus.NO_BIN).andIsDeletedEqualTo(CommonStatus.NO_DELETED).andIsFinishedEqualTo(CommonStatus.IS_FINISHED);
        List<AppFiles> fileList = appFilesMapper.selectByExample(example);

        List<Object> totalList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(folderList)) {
            for (AppFolder folder : folderList) {
                totalList.add(folder);
            }
        }
        if (CollectionUtil.isNotEmpty(fileList)) {
            for (AppFiles files : fileList) {
                totalList.add(files);
            }
        }
        List<Object> pageList = paginate(totalList, pageSize, pageNum);

        HashMap<String, Object> map = new HashMap<>();
        map.put("folderList", folderList);
        map.put("fileList", fileList);
        map.put("total", folderList.size() + fileList.size());
        map.put("pageList", pageList);
        return CommonResult.success(map);
    }

    @Override
    public CommonResult getFilesAndFolders(Long userId, Long folderId) {
        AppFolderExample exa = new AppFolderExample();
        exa.createCriteria().andUserIdEqualTo(userId).andParentIdEqualTo(folderId).andIsBinEqualTo(CommonStatus.NO_BIN).andIsDeletedEqualTo(CommonStatus.NO_DELETED);
        List<AppFolder> folderList = appFolderMapper.selectByExample(exa);

        AppFilesExample fileExa = new AppFilesExample();
        fileExa.createCriteria().andUserIdEqualTo(userId).andFolderIdEqualTo(folderId).andIsBinEqualTo(CommonStatus.NO_BIN).andIsDeletedEqualTo(CommonStatus.NO_DELETED).andIsFinishedEqualTo(CommonStatus.IS_FINISHED);
        List<AppFiles> fileList = appFilesMapper.selectByExample(fileExa);

        Map<String, List> map = new HashMap<>();

        map.put("folderList", folderList);
        map.put("fileList", fileList);
        return CommonResult.success(map);
    }

    @Override
    public CommonResult batchToBin(List<Long> idList) {
        try {
            appFolderMapper.batchToBin(idList);
        } catch (Exception e) {
            logger.error("批量入回收站失败，数据回滚", e);
            e.printStackTrace();
        }
        return CommonResult.success(ResultCode.SUCCESS);
    }

    @Override
    public CommonResult moveFolder(AppFolderVo vo) {
        if (ObjUtil.isNull(vo)) {
            return CommonResult.failed();
        }
        Long parentId = vo.getTargetId();
        Long id = vo.getId();
        try {
            if (!"folder".equals(vo.getType())) {
                AppFiles files = appFilesMapper.selectByPrimaryKey(id);
                files.setFolderId(parentId);
                appFilesMapper.updateByPrimaryKey(files);
            } else {
                AppFolder folder = appFolderMapper.selectByPrimaryKey(id);
                //在这里进行校验父文件夹不能移动到子文件夹
                AppFolder targetFolder = appFolderMapper.selectByPrimaryKey(parentId);
                while (targetFolder!= null) {
                    if (targetFolder.getId().equals(id)) {
                        // 父文件夹不能移动到子文件夹
                        return CommonResult.failed(ResultCode.MOVE_ERROR);
                    }
                    targetFolder = appFolderMapper.selectByPrimaryKey(targetFolder.getParentId());
                }
                folder.setParentId(parentId);
                appFolderMapper.updateByPrimaryKey(folder);
            }
        } catch (Exception e) {
            logger.error("====== 移动文件夹失败 ======");
            e.printStackTrace();
        }
        return CommonResult.success(ResultCode.SUCCESS);
    }

    private <T> List<T> paginate(List<T> totalList, int pageSize, int pageNumber) {
        if (totalList == null || totalList.isEmpty()) {
            return new ArrayList<>();
        }
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalList.size());
        return totalList.subList(startIndex, endIndex);
    }

    private List<Long> getAllChildFolderIds(Long folderId) {
        List<Long> result = new ArrayList<>();

        AppFolder folder = appFolderMapper.selectByPrimaryKey(folderId);
        if (folder == null) {
            return result;
        }
        result.add(folderId);

        // 递归地查找所有子文件夹的ID
        AppFolderExample example = new AppFolderExample();
        example.createCriteria().andParentIdEqualTo(folderId);
        List<AppFolder> list = appFolderMapper.selectByExample(example);//所有该目录的子文件夹

        if (list == null) {
            return result;
        } else {
            for (AppFolder app : list) {
                result.addAll(getAllChildFolderIds(app.getId()));
            }
        }
        return result;
    }

}
