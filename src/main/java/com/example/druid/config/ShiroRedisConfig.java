package com.example.druid.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Shiro 配置类 结合Redis
 *
 * @author Andrew
 */
@Configuration
public class ShiroRedisConfig {

    @Autowired
    private FebsProperties febsProperties;
    //从application.yml引入
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.database}")
    private int database;

    /**
     * shiro 中配置 redis 缓存
     *
     * @return RedisManager
     */
    private RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host + ":" + port);
        //有密码则设置
        if (StringUtils.isNoneBlank(password)) {
            redisManager.setPassword(password);
        }
        redisManager.setTimeout(timeout);
        redisManager.setDatabase(database);
        return redisManager;
    }

    //redis缓存管理类
    private RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * 创建ShrioFilterFactoryBean 过滤器工厂类
     * * 添加Shiro内置过滤器
     * *
     * * Shiro内置过滤器 可实现权限相关的拦截器
     * * 常用过滤器:
     * *
     * * anon:无须认证(登录)可以访问
     * * authc:须认证才可访问
     * * user:如果使用rememberMe的功能可直接访问
     * * perms:该资源须得到[资源]权限才可访问
     * * role:该资源须得到[角色]权限才可访问
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {

        //设置安全管理器
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //获取filters
        //Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
        // 设置 securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 登录不成功后跳转到的 url
        shiroFilterFactoryBean.setLoginUrl(febsProperties.getShiro().getLoginUrl());
        // 登录成功后跳转的 url
        shiroFilterFactoryBean.setSuccessUrl(febsProperties.getShiro().getSuccessUrl());
        // 未授权 url
        shiroFilterFactoryBean.setUnauthorizedUrl(febsProperties.getShiro().getUnauthorizedUrl());

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 设置免认证 url 列表
        String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(febsProperties.getShiro().getAnonUrl(), ",");
        for (String url : anonUrls) {
            filterChainDefinitionMap.put(url, "anon");
        }

        // 自己写退出逻辑,清空登录Redis缓存;
        // 配置退出过滤器，其中具体的退出代码 Shiro已经替我们实现了
        // filterChainDefinitionMap.put(febsProperties.getShiro().getLogoutUrl(), "logout");

        /**
         * 用在shiro thymeleaf 前端显示上
         * 在controller端要配合 @RequirePermissions使用
         * filterChainDefinitions：apache shiro通过filterChainDefinitions参数来分配链接的过滤，资源过滤有常用的以下几个参数：
         *
         * 1、authc 表示需要认证的链接
         *
         * 2、perms[/url] 表示该链接需要拥有对应的资源/权限才能访问
         *
         * 3、roles[admin] 表示需要对应的角色才能访问
         *
         * 4、perms[admin:url] 表示需要对应角色的资源才能访问
         * */
        filterChainDefinitionMap.put("/add", "perms[admin:add]");
        filterChainDefinitionMap.put("/upd", "perms[admin:upd]");
        filterChainDefinitionMap.put("/del", "perms[admin:del]");
        filterChainDefinitionMap.put("/all", "perms[admin:add,admin:upd,user:del]"); //admin的所有权限

        // 除上以外所有 url都必须认证通过才可以访问，未通过认证自动访问 LoginUrl
        // authc 表示需要认证的链接
        filterChainDefinitionMap.put("/**", "authc"); //user

        //添加过滤器
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     *
     * @将缓存对象注入到SecurityManager中： 获取Bean中的对象 @Qualifier("userRealm")
     */

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 配置 SecurityManager，并注入 shiroRealm
        securityManager.setRealm(shiroRealm());
        // 配置 shiro session管理器
        securityManager.setSessionManager(sessionManager());
        // 配置 缓存管理类 cacheManager
        securityManager.setCacheManager(cacheManager());
        // 配置 rememberMeCookie
        securityManager.setRememberMeManager(cookieRememberMeManager());
        return securityManager;
    }

    /**
     * 创建Realm
     */
    //@Bean(name = "shiroRealm")
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        return shiroRealm;
    }

    /**
     * rememberMe cookie 效果是重开浏览器后无需重新登录
     *
     * @return SimpleCookie
     */
    private SimpleCookie rememberMeCookie() {
        // 设置 cookie 名称，对应 login.html 页面的 <input type="checkbox" name="rememberMe"/>
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // 设置 cookie 的过期时间，单位为秒，这里为一天
        cookie.setMaxAge(febsProperties.getShiro().getCookieTimeout());
        return cookie;
    }


    /**
     * cookie管理对象
     *
     * @return CookieRememberMeManager
     */
    private CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());

        // rememberMe cookie 加密的密钥
        String encryptKey = "shiro_key";
        byte[] encryptKeyBytes = encryptKey.getBytes(StandardCharsets.UTF_8);
        String rememberKey = Base64Utils.encodeToString(Arrays.copyOf(encryptKeyBytes, 16));
        cookieRememberMeManager.setCipherKey(Base64.decode(rememberKey));
        return cookieRememberMeManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * session 管理对象
     *
     * @return DefaultWebSessionManager
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<>();
        listeners.add(new ShiroSessionListener());
        // 设置 session超时时间
        sessionManager.setGlobalSessionTimeout(febsProperties.getShiro().getSessionTimeout() * 1000L);
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionDAO(redisSessionDAO());
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 用于开启 Thymeleaf 中的 shiro 标签的使用
     *
     * @return ShiroDialect shiro 方言对象
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


}
