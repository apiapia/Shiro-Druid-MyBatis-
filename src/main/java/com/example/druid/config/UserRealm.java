package com.example.druid.config;

import com.example.druid.bean.Employee;
import com.example.druid.mapper.EmployeeMapper;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.util.StringUtils;

/**
 * 自定义 UserRealm
 * */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    EmployeeMapper employeeMapper;
    /**
     * 执行授权逻辑 (权限)
     *
     * */

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

        if (!"".equals(permissions) && permissions.contains(",")){
            for (String perm: StringUtils.split(permissions,",")){
                info.addStringPermission(perm);
                //打印出每一个用户的权限
                System.out.println("用户：" + user.getLastName() + "的权限：" + perm);
            }
        }else {
            info.addStringPermission(permissions);
        }
        //info.addStringPermission(emp.getPerms());
        //System.out.println(emp.getPerms());
        return info;
    }


    /**
     * 执行认证逻辑 (登录)
     *
     * */

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg) throws AuthenticationException {
        System.out.println("执行认证逻辑 (登录)");

        /**
         *
         * 编写shiro的判断逻辑 ，判断用户名和密码
         * */
        //1.判断用户名
        UsernamePasswordToken token = (UsernamePasswordToken) arg;
        Employee emp = employeeMapper.getEmpByName(token.getUsername());
        if ("".equals(emp) || emp == null){
            return  null;
        }
        //2.SimpleAuthenticationInfo 封装了判断密码
        SimpleAuthenticationInfo auth = new SimpleAuthenticationInfo(emp,emp.getPassword(),"");
        return auth;
    }
}
