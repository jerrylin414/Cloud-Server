package com.lzy.util;

import com.lzy.common.ResultCode;
import com.lzy.dao.AppFilesMapper;
import com.lzy.dao.AppFolderMapper;
import com.lzy.entity.AppFolder;
import com.lzy.entity.AppFolderExample;
import com.lzy.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther jerry
 * @date 10/7/2024 5:08 PM
 */
@Service
public class Test {
    @Autowired
    private static AppFolderMapper appFolderMapper;

    public static void main(String[] args) {
//        test();
//        throw new ServiceException(ResultCode.FAILED.getCode(),ResultCode.FAILED.getMessage());



    }

    public static void test(){
        AppFolderExample exa = new AppFolderExample();
        exa.createCriteria().andUserIdEqualTo(1l);
        List<AppFolder> list = appFolderMapper.selectByExample(exa);
//        AppFolder root = covertToTree(list);
//        System.out.println(root);
    }

//    public static AppFolder covertToTree(List<AppFolder> AppFolderList) {
//        Map<Long, AppFolder> map = new HashMap<>();
//        AppFolder root = null;
//        for (AppFolder o : AppFolderList) {
//            Long id = o.getId();
//            Long parentId = o.getParentId();
//            AppFolder AppFolder = new AppFolder(id, o.getName(), parentId);
//            map.put(id, AppFolder);
//            if (parentId == -1) {
//                root = AppFolder;
//            } else {
//                AppFolder AppFolder1 = map.get(parentId);
//                if (AppFolder1 != null) {
//                    AppFolder1.addChild(AppFolder);
//                }
//            }
//        }
//        return root;
//    }
}
