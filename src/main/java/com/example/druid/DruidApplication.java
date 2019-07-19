package com.example.druid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
/**
 * 扫描resources内的mapper的xml文件;
 * */
@MapperScan(value = "com.example.druid.mapper")

/**
 *
 * 开启缓存 导入ehcache-core-2.4.8.jar包
 * 程序应用开发时，使用redis
 * ehcache，用来自动缓存shiro-cache 及 mybatis-cache
 *
 * */
@EnableCaching
public class DruidApplication {

	public static void main(String[] args) {
		SpringApplication.run(DruidApplication.class, args);
	}

}
