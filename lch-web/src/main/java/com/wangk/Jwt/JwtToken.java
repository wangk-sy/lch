package com.wangk.Jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName :JwtToken
 * @Description :TODO
 * @Author :16388
 * @Date :2020/6/9 10:03
 * @Version :1.0
 **/
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = -2272193771485900173L;
    /**
     * Token
     */
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
