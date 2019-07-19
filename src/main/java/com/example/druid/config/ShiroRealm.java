package com.example.druid.config;

import com.example.druid.bean.Employee;
import com.example.druid.mapper.EmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils; //引入shiro的SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.util.StringUtils;

/**
 * 自定义 ShiroRealm
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    ShiroRedisConfig shiroRedisConfig;
    /**
     * 执行授权逻辑 (权限)
     */

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /**
         * 引入Redis或者Ehcache缓存 才不会连续打印三次
         *
         * */
        System.out.println("执行授权逻辑 (权限)");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        Subject currentUser = SecurityUtils.getSubject();
        Employee user = (Employee) currentUser.getPrincipal();
        /*
         * 用户权限列表   https://www.cnblogs.com/hoge66/p/9923476.html
         * */
        String permissions = user.getPerms();

        if (!"".equals(permissions) && permissions.contains(",")) {
            for (String perm : StringUtils.split(permissions, ",")) {
                info.addStringPermission(perm);
                //打印出每一个用户的权限
                System.out.println("用户：" + user.getLastName() + "的权限：" + perm);
            }
        } else {
            info.addStringPermission(permissions);
        }
        //info.addStringPermission(emp.getPerms());
        //System.out.println(emp.getPerms());
        return info;
    }


    /**
     * 用户认证(登录)
     *
     * @param token AuthenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行认证逻辑 (登录)");

        /**
         *
         * 编写shiro的判断逻辑 ，判断用户名和密码
         * */
        // 获取用户输入的用户名和密码
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        // 1.通过用户名到数据库查询用户信息
        Employee user = employeeMapper.getEmpByName(username);
        if ("".equals(user) || user == null)
            throw new UnknownAccountException("用户不存在！");
        //2.判断密码
        if (!StringUtils.equals(password, user.getPassword()))
            throw new IncorrectCredentialsException("密码错误！");
//       if (User.STATUS_LOCK.equals(user.getStatus()))
//            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        //3.返回user对象
        return new SimpleAuthenticationInfo(user, user.getPassword(), "");
    }

    /**
     * 清除当前用户权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其 clearCache方法。
     */

    public void clearCache() {
        //清除单个sessionID
        log.info("清除Redis单个登录的sessionID:开始");
        Subject currentUser = SecurityUtils.getSubject();
        Session session = (Session) currentUser.getSession();
        shiroRedisConfig.redisSessionDAO().delete(session);
        log.info("清除session成功,sessionID:" + session.getId());
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

}
