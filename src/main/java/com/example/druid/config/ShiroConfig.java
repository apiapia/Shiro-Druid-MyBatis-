package com.example.druid.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * ShiroConfig 配置类 @Configuration注入
 * */
@Configuration
public class ShiroConfig {

    /**
     * 创建ShrioFilterFactoryBean
     * */

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        /**
         * 设置安全管理器
         * */
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        /**
         * 添加Shiro内置过滤器
         *
         * Shiro内置过滤器 可实现权限相关的拦截器
         * 常用过滤器:
         *
         * anon:无须认证(登录)可以访问
         * authc:须认证才可访问
         * user:如果使用rememberMe的功能可直接访问
         * perms:该资源须得到[资源]权限才可访问
         * role:该资源须得到[角色]权限才可访问
         * */

        Map<String,String> filterMap = new LinkedHashMap<>();

        //须要放行的所有页面
        filterMap.put("/welcome","anon");
        filterMap.put("/login","anon");
        filterMap.put("/getVerifyCode","anon"); //getVerifyCode kaptcha验证码;
        filterMap.put("/druid/**","anon");

        //会员注册
        filterMap.put("/register","anon");

        //文件上传
        filterMap.put("/file","anon");
        filterMap.put("/fileupload","anon");


        //退出登录
        filterMap.put("/logout","logout");

        /**
         * 拦截URL
         * */
        filterMap.put("add","perms[admin:add]");
        filterMap.put("upd","perms[admin:upd]");
        filterMap.put("del","perms[admin:del]");


        //放行.js,*.css等;
        filterMap.put("/static/**","anon");
        //禁止所有资源
        filterMap.put("/**","authc");

        //修改默认登录页面;
        shiroFilterFactoryBean.setLoginUrl("/welcome");
        //修改默认未授权提示页面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/unAuth");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     * @将缓存对象注入到SecurityManager中：
     * 获取Bean中的对象 @Qualifier("userRealm")
     * */

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(
            @Qualifier("userRealm") UserRealm userRealm,
            @Qualifier("shiroEhCacheManager") EhCacheManager shiroEhCacheManager){

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联注入realm
        securityManager.setRealm(userRealm);
        //关联ehcache
        securityManager.setCacheManager(shiroEhCacheManager);
        return securityManager;

    }
    /**
     *
     * 创建Realm
     * */
    @Bean(name = "userRealm")
    public  UserRealm getRealm(){
        UserRealm userRealm = new UserRealm();
        return  userRealm;
    }

    /**
     * 配置ShiroDialect 用于 thymeleaf和shiro标签的配合使用
     * */
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

    /**
     * @ShiroConfig中注入Ehcache缓存
     *
     * */
    @Bean(name = "shiroEhCacheManager")
    public EhCacheManager getEhCacheManger(){
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:config/shiro-ehcache.xml");
        return  em;
    }
}
