package com.example.druid.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class DruidConfig {

    /**
     * 配置DruidConfig
     * */
    @ConfigurationProperties(prefix = "druid")
    @Bean
    public DataSource druid() {
        return new DruidDataSource();
    }

    //1.配置一个管理后台的Servlet
   @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();

        reg.setServlet(new StatViewServlet());
       // 登录URL http://localhost:8080/druid/login.html
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("allow","");
        /*开启用户名与密码*/
        reg.addInitParameter("loginUsername","root");
        reg.addInitParameter("loginPassword","root");

        return reg;


    }

   //2.配置一个监控的filter,监控网站的数据库查询
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean bean =  new FilterRegistrationBean();
       /* bean.setFilter(new WebStatFilter());
        Map<String,String> map = new HashMap<>();
        map.put("exclusions","*.js,*.css,*.html,/*");
        bean.setInitParameters(map);
        bean.setUrlPatterns(Arrays.asList("/*"));*/
       bean.setFilter(new WebStatFilter());
       bean.addUrlPatterns("/*");
       bean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return bean;
    }


}
