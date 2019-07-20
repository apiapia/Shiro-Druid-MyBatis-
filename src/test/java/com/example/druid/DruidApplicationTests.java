package com.example.druid;

import com.example.druid.bean.Employee;
import com.example.druid.config.RedisCache;
import com.example.druid.mapper.EmployeeMapper;
import com.example.druid.untils.HttpRequest;
import com.example.druid.untils.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.PrimitiveIterator;

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
            //先开启分页
            PageHelper.startPage(1, 3); //pageNum当前页,pageSize:每页的显示的条数;
            List<Employee> employeeList = sqlSession.selectList("getEmps");
            logger.warn("返回的数量：" + employeeList.size());
            //然后把查询结果集放入PageInfo中
            PageInfo<Employee> info = new PageInfo<>(employeeList);

            for (Employee emp : employeeList) {
                logger.info(emp.getEmail());
            }
            System.out.println("总页数:" + info.getPages() + "当前页:" + info.getPageNum());

        } finally {
            sqlSession.close();
        }

    }

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * Redis中的存储
     * Redis会自动的将Sql+条件+Hash等当做key值，而将查询结果作为value，
     * 只要请求中的所有参数都符合，
     * 那么就会使用redis中的二级缓存。
     * */
    @Test
    public void selectEmpCache() throws IOException {

        //修改log4j(log4j.properties)的级别为trace/debug日志，记录mysql查询语句，
        // 检查是否已经查了二次;
        //缓存生效后，则不会再有sql查询语句
        //DEBUG - ==>  Preparing: SELECT id,lastName,password
        // String lastName = "a" + "%";
        String lastName = "a";
        List<Employee> empList = employeeMapper.getEmpLikeName(lastName);
        for (Employee e : empList) {
            System.out.println("查询用户:" + e.getEmail());
        }

        lastName = "j";
        List<Employee> empList2 = employeeMapper.getEmpLikeName(lastName);
        for (Employee e : empList2) {
            System.out.println("查询用户:" + e.getEmail());
        }

    }

    /**
     * @Test 测试Redis是否引入成功 ,记得引入@Autowired自动加载
     */
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void testRedis() {
        redisUtil.set("k1", "{user:'中文乱码',age:30}");
        String k1 = redisUtil.get("k1");
        logger.info(k1);
    }
}
