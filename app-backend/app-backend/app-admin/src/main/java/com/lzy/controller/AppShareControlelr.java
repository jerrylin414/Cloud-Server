package com.lzy.controller;

import com.lzy.common.CommonResult;
import com.lzy.service.impl.AppShareServiceImpl;
import com.lzy.vo.AppFilesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @auther jerry
 * @date 22/7/2024 4:27 PM
 */
@RestController
@RequestMapping("/share")
public class AppShareControlelr {
    @Autowired
    private AppShareServiceImpl appShareService;

    //show出全部文件夹
    @RequestMapping(value = "/showAll", method = RequestMethod.GET)
    public CommonResult showAll(@RequestParam Long userId) {
        return appShareService.showAll(userId);
    }

    @RequestMapping(value = "/getCode", method = RequestMethod.GET)
    public CommonResult getCode(@RequestParam String code) {
        return appShareService.getCode(code);
    }

    @RequestMapping(value = "/createShare", method = RequestMethod.POST)
    public CommonResult createShare(@RequestBody AppFilesVo vo) {
        return appShareService.createShare(vo);
    }
}
