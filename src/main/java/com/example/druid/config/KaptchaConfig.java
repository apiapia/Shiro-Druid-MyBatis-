package com.example.druid.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config; //引入google的config配置;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

//参考：https://www.jianshu.com/p/ad0bc0508abd

@Component
public class KaptchaConfig {

    private final static String CODE_LENGTH = "4";
    private final static String SESSION_KEY = "verification_session_key";

    @Bean
    public DefaultKaptcha defaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 设置边框
        properties.setProperty("kaptcha.border", "no");
        // 设置边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 设置字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        // 设置图片宽度
        properties.setProperty("kaptcha.image.width", "180");
        // 设置图片高度
        properties.setProperty("kaptcha.image.height", "40");
        // 设置字体尺寸
        properties.setProperty("kaptcha.textproducer.font.size", "35");
        // 设置图片样式
        properties.setProperty("kaptcha.obscurificator.impl","com.google.code.kaptcha.impl.ShadowGimpy");
        // 设置session key
        properties.setProperty("kaptcha.session.key", SESSION_KEY);
        // 设置验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", CODE_LENGTH);
        // 设置字体
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,黑体");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
