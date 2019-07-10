package com.example.druid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * 扫描resources内的mapper的xml文件;
 * */
@MapperScan(value = "com.example.druid.mapper")
public class DruidApplication {

	public static void main(String[] args) {
		SpringApplication.run(DruidApplication.class, args);
	}

}
