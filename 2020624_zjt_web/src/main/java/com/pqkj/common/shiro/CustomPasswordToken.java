package com.pqkj.common.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CustomPasswordToken extends UsernamePasswordToken {
    private String token;

    public CustomPasswordToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
}
