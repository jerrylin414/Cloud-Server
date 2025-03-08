package com.lzy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.lzy.common.CommonResult;
import com.lzy.common.ResultCode;
import com.lzy.dao.AppUserMapper;
import com.lzy.entity.AppUser;
import com.lzy.entity.AppUserExample;
import com.lzy.exception.ServiceException;
import com.lzy.service.AppUserService;
import com.lzy.util.TokenUtil;
import com.lzy.vo.AppUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService {
    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public AppUser getById(Long userId) {
        AppUserExample example = new AppUserExample();
        example.createCriteria().andIdEqualTo(userId);
        List<AppUser> users = appUserMapper.selectByExample(example);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public CommonResult addUser(AppUser appUser) {
        if (ObjUtil.isEmpty(appUser) || StrUtil.isBlank(appUser.getUsername()) || StrUtil.isBlank(appUser.getUsername())){
            return CommonResult.failed();
        }else {
            //check username
            String s = SecureUtil.md5(appUser.getPassword());
            AppUserExample example = new AppUserExample();
            example.createCriteria().andUsernameEqualTo(appUser.getUsername());
            List<AppUser> appUsers = appUserMapper.selectByExample(example);
            if (appUsers.size() > 1){
                return CommonResult.failed("用户名已经被注册!");
            }
        }

        try{
            String md5Pwd = SecureUtil.md5(appUser.getPassword());
            appUser.setPassword(md5Pwd);
            appUser.setCreateTime(new Date());
            appUserMapper.insert(appUser);
        }catch (Exception e){
            throw new ServiceException(ResultCode.FAILED.getCode());
        }
        return CommonResult.success(ResultCode.SUCCESS);
    }

    @Override
    public CommonResult login(AppUserVo appUserVo) {
        if (ObjUtil.isEmpty(appUserVo)){
            return CommonResult.failed();
        }
        String username = appUserVo.getUsername();
        String password = appUserVo.getPassword();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return CommonResult.failed();
        }
        try{
            String md5Pwd = SecureUtil.md5(password);
            AppUserExample example = new AppUserExample();
            example.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(md5Pwd);
            List<AppUser> appUsers = appUserMapper.selectByExample(example);
            AppUser appUser = new AppUser();
            if (appUsers.isEmpty()){
                return CommonResult.failed(ResultCode.ERROR_USER_OR_PWD);
            }else {
                appUser = appUsers.get(0);
            }

            String token = TokenUtil.getToken(appUser.getId(), appUser.getPassword());
            BeanUtils.copyProperties(appUser,appUserVo);
            appUserVo.setToken(token);
            return CommonResult.success(appUserVo,ResultCode.SUCCESS.getMessage());
        }catch (Exception e){
            throw new ServiceException(ResultCode.FAILED.getCode());
        }
    }
}
