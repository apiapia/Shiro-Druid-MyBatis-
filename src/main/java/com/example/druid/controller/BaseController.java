package com.example.druid.controller;

/**
 * 技术参考：https://www.jianshu.com/p/ad0bc0508abd
 */

import com.example.druid.bean.Employee;
import com.example.druid.config.ShiroRealm;
import com.example.druid.untils.MD5Util;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Controller
public class BaseController {

    @Autowired
    ShiroRealm shiroRealm;
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("msg", "SHIRO的世界");
        return "welcome";
    }

    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("msg", "add");
        return "add";
    }

    @RequestMapping("/upd")
    public String upd(Model model) {
        model.addAttribute("msg", "upd");
        return "upd";
    }

    @GetMapping("/toLogout")
    public String toLogout(Model model) {
    Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            //清除Redis缓存
            log.info("成功清除Redis缓存");
            shiroRealm.clearCache();
            //退出登录
            subject.logout();
            model.addAttribute("msg", "已退出");
            return "redirect:/";
        }
        return "";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("msg", "请登录");
        return "login";
    }


    @GetMapping("/admin")
    public String admin(Model model) {
        Subject currentUser = SecurityUtils.getSubject();
        //sessionid
        System.out.println("currentUser.getSession().getId()" + currentUser.getSession().getId());
        Employee user = (Employee) currentUser.getPrincipal();
        model.addAttribute("msg", "[" + user.getLastName() + "]登录成功");
        return "admin";
    }

}
