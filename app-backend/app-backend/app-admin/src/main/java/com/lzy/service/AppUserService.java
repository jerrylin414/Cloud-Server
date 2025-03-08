package com.lzy.service;

import com.lzy.common.CommonResult;
import com.lzy.entity.AppUser;
import com.lzy.vo.AppUserVo;

public interface AppUserService {
    AppUser getById(Long userId);

    CommonResult addUser(AppUser appUser);

    CommonResult login(AppUserVo appUserVo);
}
