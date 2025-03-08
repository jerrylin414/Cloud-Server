package com.lzy.controller;

import com.lzy.common.CommonResult;
import com.lzy.entity.AppUser;
import com.lzy.service.AppUserService;
import com.lzy.service.impl.AppUserServiceImpl;
import com.lzy.vo.AppUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AppUserController {
    @Autowired
    private AppUserServiceImpl appUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult login(@RequestBody AppUserVo appUserVo) {
        return appUserService.login(appUserVo);
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public CommonResult addUser(@RequestBody AppUser appUser) {
        return appUserService.addUser(appUser);
    }


}
