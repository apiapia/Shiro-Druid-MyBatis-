package com.example.druid.config;

import lombok.Data;

@Data
public class ShiroProperties {

    private long sessionTimeout;
    private int cookieTimeout;
    private String anonUrl;
    private String loginUrl;
    private String successUrl;
    private String logoutUrl;
    private String unauthorizedUrl;

    // setter getter 方法 ，已由 lombok 自动生成;

//    public String getLoginUrl() {
//        return loginUrl;
//    }
//
//    public void setLoginUrl(String loginUrl) {
//        this.loginUrl = loginUrl;
//    }
}
