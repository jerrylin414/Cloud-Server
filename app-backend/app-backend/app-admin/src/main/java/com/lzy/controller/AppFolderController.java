package com.lzy.controller;

import com.lzy.common.CommonResult;
import com.lzy.entity.AppFolder;
import com.lzy.service.impl.AppFolderServiceImpl;
import com.lzy.vo.AppFilesVo;
import com.lzy.vo.AppFolderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/folder")
public class AppFolderController {
    @Autowired
    private AppFolderServiceImpl appFolderService;

    @RequestMapping(value = "/firstShow", method = RequestMethod.GET)
    public CommonResult firstShow(@RequestParam Long userId,
                                  @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){
        return appFolderService.firstShow(userId,pageNum,pageSize);
    }

    @RequestMapping(value = "/getFolderFiles", method = RequestMethod.GET)
    public CommonResult getFolderFiles(@RequestParam Long userId,@RequestParam Long folderId){
        return appFolderService.getFilesAndFolders(userId,folderId);
    }

    @RequestMapping(value = "/addFolder", method = RequestMethod.POST)
    public CommonResult addFolder(@RequestParam String folderName,@RequestParam Long userId,
                                  @RequestParam(value = "parentId",defaultValue = "0") Long partendId){
        return appFolderService.addFolder(folderName,userId,partendId);
    }

    @RequestMapping(value = "/editFolder", method = RequestMethod.POST)
    public CommonResult editFolder(@RequestParam Long folderId,@RequestParam String newName){
         return appFolderService.editFolder(folderId,newName);
    }

    @RequestMapping(value = "/toBin", method = RequestMethod.POST)
    public CommonResult toBin(@RequestBody AppFilesVo appFilesVo){
        return appFolderService.deleteFolder2Bin(appFilesVo);
    }

    @RequestMapping(value = "/deleteFolder", method = RequestMethod.POST)
    public CommonResult deleteFolder(@RequestBody AppFilesVo vo){
        return appFolderService.deleteFolder2Bin(vo);
    }

    @RequestMapping(value = "/moveFolder", method = RequestMethod.POST)
    public CommonResult moveFolder(@RequestBody AppFolderVo vo){
        return appFolderService.moveFolder(vo);
    }

}
