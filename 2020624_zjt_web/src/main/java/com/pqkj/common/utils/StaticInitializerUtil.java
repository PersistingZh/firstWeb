package com.pqkj.common.utils;


import org.springframework.stereotype.Component;

/**
 * 注入tokenSettings值
 */
@Component
public class StaticInitializerUtil {
   private TokenSettings tokenSettings;

    public StaticInitializerUtil(TokenSettings tokenSettings) {
        JwtTokenUtil.setTokenSettings(tokenSettings);
    }
}
