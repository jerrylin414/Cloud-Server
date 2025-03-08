package com.lzy.util;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @auther jerry
 * @date 11/7/2024 2:40 PM
 */

@Component
public class TokenUtil {

    public static String getToken(Long userId,String sign){
        return JWT.create().withAudience(String.valueOf(userId))
                .withExpiresAt(DateUtil.offsetHour(new Date(),2))// 2 hours
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 验证token 合法性
     */
    public static DecodedJWT verify(String token,String sign) {
        //如果有任何验证异常，此处都会抛出异常
        return JWT.require(Algorithm.HMAC256(sign)).build().verify(token);
    }

    /**
     * 获取token信息方法
     */
    public static Map<String, Claim> getTokenInfo(String token,String sign) {
        return JWT.require(Algorithm.HMAC256(sign)).build().verify(token).getClaims();
    }
}
