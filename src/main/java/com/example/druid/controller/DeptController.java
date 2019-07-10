package com.example.druid.controller;

import com.example.druid.bean.Department;
import com.example.druid.bean.Employee;
import com.example.druid.mapper.DepartmentMapper;
import com.example.druid.mapper.EmployeeMapper;
import com.example.druid.untils.MD5Util;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils; // 引入Shiro的SecurityUtils插件;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Security;


@Controller
public class DeptController {

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    EmployeeMapper employeeMapper;


    @GetMapping("/emp/{id}")
    public Employee getEmp(@PathVariable("id") Integer id) {
        return employeeMapper.getEmpById(id);
    }

    @GetMapping("/dept/del/{id}")
    public  int delDept(@PathVariable("id") Integer id){
        return  departmentMapper.deleteDeptById(id);
    }
    @GetMapping("/dept/{id}")
    public Department getDept(@PathVariable("id") Integer id) {
        return departmentMapper.getDeptById(id);
    }


    @GetMapping("/emp/add")
    public  Object insertEmp(
            @RequestParam(value = "email",required = false) String email,
            @RequestParam("lastName") String lastName,
            @RequestParam("departmentId") Integer depId,
            Employee employee) throws Exception{

        System.out.println("打印值："+email + "-" + lastName + "-" + depId);

        if ("".equals(email) || email == null){
            return "email不能为空";
        }

        employeeMapper.insertEmp(employee);
        int id = employee.getId();
        System.out.println("取得自增ID:"+ id);
        return employee;
    }
}
