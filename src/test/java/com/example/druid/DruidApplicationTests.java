package com.example.druid;

import com.example.druid.bean.Employee;
import com.example.druid.untils.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest

public class DruidApplicationTests {

    //记得注入 Autowired
    @Autowired
    DataSource dataSource;
    Logger logger = LoggerFactory.getLogger(getClass());

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void init() {
        try {
            Reader reader = Resources.getResourceAsReader("mybatis/mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            reader.close();
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }


    @Test
    public void contextLoads() throws SQLException {
        //查看dataSource注册源
        System.out.println(dataSource.getClass());

        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }

    @Test
    public void curlGet() {
        String url = "http://www.baidu.com";
        String data = ""; //key=123&v=456
        String result = HttpRequest.doGet(url, data);
        System.out.println(result);

    }


    @Test
    public void slf4j() {
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

    @Test
    public void selectAll() throws IOException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Employee> employeeList = sqlSession.selectList("getEmps");
            for (Employee emp:employeeList) {
                logger.info(emp.getEmail());
            }

        }finally {
            sqlSession.close();
        }

    }

    @Test
    public void selectEmpCache() throws IOException{
        //新建一个sqlSession
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();

        //修改log4j(log4j.properties)的级别为trace日志，记录mysql查询语句，检查是否已经查了二次;
        try{
            Employee user01 =  sqlSession1.selectOne("getEmpById",1);
            System.out.println(user01);
        }finally {
            sqlSession1.close();
        }

        try{
            Employee user02 =  sqlSession2.selectOne("getEmpById",1);
            System.out.println(user02);
        }finally {
            sqlSession2.close();
        }
    }

}
