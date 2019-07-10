package com.example.druid.controller;

import com.example.druid.untils.MD5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private String SHIRO_VERIFY_SESSION = "verifySessionCode";

    // @RequestMapping(value = "/login",method = RequestMethod.POST)
    @PostMapping(value = "login")
    public String login(
            @RequestParam(value = "lastName") String lastName,
            @RequestParam("password") String password,
            String verifyCode,  // 验证码
            boolean rememberMe, // 是否记住我
            Model model){

        /**
         *
         * 使用Shiro编写认证操作
         * */

        //1.新建一个token
        // 加密密码
        String md5Pwd =  MD5Util.encrypt(lastName,password);
        //System.out.println("md5Pwd" + md5Pwd);
        UsernamePasswordToken token = new UsernamePasswordToken(lastName,md5Pwd);

        //2.shiro的SecurityUtils工具 获取subject对象; 注意区分Spring的SecurityUtils;
        Subject currentUser = SecurityUtils.getSubject();

        // 获取session中的验证码
        String sessionVerifyCode = (String) currentUser.getSession().getAttribute(SHIRO_VERIFY_SESSION);
        if ("".equals(verifyCode) || !sessionVerifyCode.equals(verifyCode)){
            model.addAttribute("msg","验证码有误");
            return "welcome";
        }

        //3.执行登t录方法
        try {
            //跳转到UserRealm的验证规则;
            currentUser.login(token);
            //登录成功
            model.addAttribute("msg","登录成功");
            //须重定
            return "redirect:/admin";
        }catch (UnknownAccountException e) {
            //e.printStackTrace();
            //登录失败:用户名不存在
            model.addAttribute("msg","用户名不存在");
            return  "welcome";
        } catch (IncorrectCredentialsException e) {
            //登录失败：密码错误;
            model.addAttribute("msg","密码有误");
            return "welcome";
        }

    }
}
