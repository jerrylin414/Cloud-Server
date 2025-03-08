package com.lzy.vo;

import com.lzy.entity.AppUser;
import lombok.Data;

/**
 * @auther jerry
 * @date 11/7/2024 5:26 PM
 */

@Data
public class AppUserVo extends AppUser {
    private String token;
}
