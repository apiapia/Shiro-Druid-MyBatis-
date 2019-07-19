package com.example.druid.config;


import lombok.Data;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

//新建类 FebsProperties
//引入配置的文件
@SpringBootApplication
@PropertySource(value = {"classpath:febs.properties"})
@ConfigurationProperties(prefix = "febs")
@Data
public class FebsProperties {

    //引类ShiroProperties类文件
    private ShiroProperties shiro = new ShiroProperties();
    private  boolean openLog4j = true;
}
