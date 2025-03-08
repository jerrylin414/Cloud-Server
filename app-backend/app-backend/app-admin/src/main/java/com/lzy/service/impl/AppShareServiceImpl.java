package com.lzy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lzy.common.CommonResult;
import com.lzy.common.CommonStatus;
import com.lzy.common.ResultCode;
import com.lzy.dao.AppFilesMapper;
import com.lzy.dao.AppFolderMapper;
import com.lzy.dao.AppShareMapper;
import com.lzy.entity.*;
import com.lzy.service.AppShareService;
import com.lzy.vo.AppFilesVo;
import com.lzy.vo.AppFolderVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @auther jerry
 * @date 22/7/2024 12:46 PM
 */
@Service
public class AppShareServiceImpl implements AppShareService {
    private static Logger logger = LogManager.getLogger(AppShareServiceImpl.class);

    @Autowired
    private AppShareMapper appShareMapper;

    @Autowired
    private AppFolderMapper appFolderMapper;

    @Autowired
    private AppFilesMapper appFilesMapper;

    @Override
    public CommonResult createShare(AppFilesVo vo) {
        if (ObjUtil.isNull(vo)) {
            return CommonResult.failed();
        }
        String shareTime = vo.getShareTime();// 1 10 30 天
        try {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            AppShare appShare = new AppShare();
            appShare.setFileId(vo.getFileId());
            appShare.setUserId(vo.getUserId());
            appShare.setName(vo.getName());
            appShare.setCount(0);
            appShare.setCode(uuid);
            appShare.setIsShow("Y");
            appShare.setShareTime(new Date());
            appShare.setEndTime(getEndTime(shareTime));
            if (CommonStatus.SHARE_FOLDER.equals(vo.getFileType())) {
                appShare.setType(CommonStatus.SHARE_FOLDER);
            } else if (CommonStatus.SHARE_FILE.equals(vo.getFileType())){
                appShare.setType(CommonStatus.SHARE_FILE);
            }
            appShareMapper.insert(appShare);
            return CommonResult.success(appShare);
        } catch (Exception e) {
            logger.error("======= 创建分享链接失败 =======");
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    public CommonResult getCode(String code) {
        if (StrUtil.isBlank(code)) {
            return CommonResult.failed();
        }
        Map<String, List> map = new HashMap<>();
        try {
            AppShareExample exa = new AppShareExample();
            exa.createCriteria().andCodeEqualTo(code).andIsShowEqualTo("Y");
            List<AppShare> list = appShareMapper.selectByExample(exa);
            AppShare app = null;
            if (list != null && list.size() > 0){
                app = list.get(0);
            }
            if (app == null) {
                logger.error("======= 失败啦~~~链接已失效 ========");
                return CommonResult.failed();
            }
            if (app.getType().equals(CommonStatus.SHARE_FOLDER)) {
                AppFolderExample afe = new AppFolderExample();
                afe.createCriteria().andUserIdEqualTo(app.getUserId()).andIdEqualTo(app.getFileId()).andIsBinEqualTo(CommonStatus.NO_BIN);
                List<AppFolder> folderList = appFolderMapper.selectByExample(afe);
                //指定文件夹下的文件
                List<AppFiles> fileList = new ArrayList<>();
                for (AppFolder folder : folderList) {
                    AppFilesExample fileExa = new AppFilesExample();
                    fileExa.createCriteria().andUserIdEqualTo(app.getUserId()).andFolderIdEqualTo(folder.getId()).andIsBinEqualTo(CommonStatus.NO_BIN).andIsFinishedEqualTo(CommonStatus.IS_FINISHED);
                    List<AppFiles> files = appFilesMapper.selectByExample(fileExa);
                    for (AppFiles file : files) {
                        fileList.add(file);
                    }
                }
                map.put("folderList", folderList);
                map.put("fileList", fileList);
            } else if (app.getType().equals(CommonStatus.SHARE_FILE)){
                AppFilesExample fileExa = new AppFilesExample();
                fileExa.createCriteria().andUserIdEqualTo(app.getUserId()).andIdEqualTo(app.getFileId()).andIsBinEqualTo(CommonStatus.NO_BIN).andIsFinishedEqualTo(CommonStatus.IS_FINISHED);
                List<AppFiles> fileList = appFilesMapper.selectByExample(fileExa);
                map.put("fileList", fileList);
                map.put("folderList", new ArrayList());
            } else{
                map.put("fileList", new ArrayList());
                map.put("folderList", new ArrayList());
            }
            app.setCount(app.getCount() + 1);
            appShareMapper.updateByPrimaryKey(app);
        } catch (Exception e) {
            logger.error("======= 更新分享链接失败 =======");
            e.printStackTrace();
        }
        return CommonResult.success(map);
    }

    @Override
    public CommonResult showAll(Long userId) {
        if (ObjUtil.isEmpty(userId)) {
            return CommonResult.failed();
        }

        AppFolderExample example = new AppFolderExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<AppFolder> all = appFolderMapper.selectByExample(example);

        List<AppFolder> list = new ArrayList<>();
        List<AppFolderVo> voList = new ArrayList<>();
        for (AppFolder appFolder : all) {
            AppFolderVo vo = new AppFolderVo();
            BeanUtil.copyProperties(appFolder, vo);
            list.add(appFolder);
            voList.add(vo);
        }
        //关键核心递归构建部门树
        List<AppFolderVo> res = buildeDepetTree(voList);
        return CommonResult.success(res);
    }

    private Date getEndTime(String shareTime) {
        LocalDate now = LocalDate.now();
        Date date = new Date();
        if ("1".equals(shareTime)) {
            LocalDate one = now.plusDays(1);
            ZonedDateTime zonedDateTime = one.atStartOfDay(ZoneId.systemDefault());
            date = Date.from(zonedDateTime.toInstant());
        } else if ("10".equals(shareTime)) {
            LocalDate ten = now.plusDays(10);
            ZonedDateTime zonedDateTime = ten.atStartOfDay(ZoneId.systemDefault());
            date = Date.from(zonedDateTime.toInstant());
        } else if ("30".equals(shareTime)) {
            LocalDate month = now.plusMonths(1);
            ZonedDateTime zonedDateTime = month.atStartOfDay(ZoneId.systemDefault());
            date = Date.from(zonedDateTime.toInstant());
        }

        return date;
    }

    private List<AppFolderVo> buildeDepetTree(List<AppFolderVo> depts) {
        //存放构建好的部门树
        ArrayList<AppFolderVo> deptTrees = new ArrayList<>();

        //存放顶级父部门
        ArrayList<AppFolderVo> parents = new ArrayList<>();

        //寻找顶级父部门 可以优化成stream流的写法
        for (AppFolderVo dept : depts) {
            AppFolderVo vo = buildeParent(dept, depts);
            if (!parents.contains(vo)) {
                parents.add(vo);
            }
        }

        for (AppFolderVo parent : parents) {
            AppFolderVo appFolderVo = buildTree(parent, depts);
            deptTrees.add(appFolderVo);
        }

        return deptTrees;
    }

    private AppFolderVo buildTree(AppFolderVo parent, List<AppFolderVo> depts) {
        ArrayList<AppFolderVo> childrens = new ArrayList<>();
        for (AppFolderVo vo : depts) {
            if (parent.getId().equals(vo.getParentId())) {
                childrens.add(buildTree(vo, depts));
            }
        }
        parent.setChildrenFolders(childrens);
        return parent;
    }

    //
    private AppFolderVo buildeParent(AppFolderVo dept, List<AppFolderVo> depts) {
        AppFolderVo parent = dept;
        for (AppFolderVo folderVo : depts) {
            if (dept.getParentId().equals(folderVo.getId())) {
                parent = buildeParent(folderVo, depts);
            }
        }
        return parent;
    }

}
