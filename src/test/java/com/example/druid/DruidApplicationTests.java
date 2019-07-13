package com.example.druid;

import com.example.druid.untils.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest

public class DruidApplicationTests {

    //记得注入 Autowired
    @Autowired
    DataSource dataSource;
    Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void contextLoads() throws SQLException {
        //查看dataSource注册源
        System.out.println(dataSource.getClass());

        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }

    @Test
    public  void curlGet(){
        String url = "http://www.baidu.com";
        String data = ""; //key=123&v=456
        String result = HttpRequest.doGet(url,data);
        System.out.println(result);

    }


    @Test
    public  void slf4j(){
        /**
         * 日志级别
         * 由低到高 trace < debug < info < warn < error
         * */
        logger.trace("这是trace");
        logger.debug("debug");
        //springboot log4j 默认使用的是info级别
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }

}
