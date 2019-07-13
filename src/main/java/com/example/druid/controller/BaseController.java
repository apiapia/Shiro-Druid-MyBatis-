package com.example.druid.controller;

/**
 *
 * 技术参考：https://www.jianshu.com/p/ad0bc0508abd
 *
 * */
import com.example.druid.bean.Employee;
import com.example.druid.untils.MD5Util;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class BaseController {

    @RequestMapping("/welcome")
    public String login(Model model){
        model.addAttribute("msg","SHIRO的世界");
        return "welcome";
    }

    @RequestMapping("/add")
    public String add(Model model){
        model.addAttribute("msg","add");
        return "add";
    }

    @RequestMapping("/upd")
    public String upd(Model model){
        model.addAttribute("msg","upd");
        return "upd";
    }

    @GetMapping("/logout")
    public String logout(Model model){

        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()){
            subject.logout();
            model.addAttribute("msg","已退出");
            return "redirect:/welcome";
        }
        return "";
    }

    @GetMapping("/admin")
    public String admin(Model model){
        Subject currentUser = SecurityUtils.getSubject();
        //sessionid
        System.out.println("currentUser.getSession().getId()" + currentUser.getSession().getId());
        Employee user = (Employee) currentUser.getPrincipal();
        model.addAttribute("msg","[" + user.getLastName() + "]登录成功");
        return "admin";
    }

}
