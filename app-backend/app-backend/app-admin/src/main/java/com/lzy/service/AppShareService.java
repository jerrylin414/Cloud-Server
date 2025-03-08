package com.lzy.service;

import com.lzy.common.CommonResult;
import com.lzy.vo.AppFilesVo;

/**
 * @auther jerry
 * @date 22/7/2024 12:46 PM
 */
public interface AppShareService {
    //生成分享
    CommonResult createShare(AppFilesVo vo);

    //增加count
    CommonResult getCode(String code);

    //展示所有文件夹目录
    CommonResult showAll(Long userId);
}
