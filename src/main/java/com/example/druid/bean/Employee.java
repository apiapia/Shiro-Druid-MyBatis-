package com.example.druid.bean;

import lombok.Data;
import java.io.Serializable;

@Data
public class Employee implements Serializable {
    private Integer id;
    private String lastName;
    private String password;
    private String email;
    private Integer departmentId;
    private String perms;

//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public Integer getDepartmentId() {
//        return departmentId;
//    }
//
//    public void setDepartmentId(Integer departmentId) {
//        this.departmentId = departmentId;
//    }
//
//    public String getPerms() {
//        return perms;
//    }
//
//    public void setPerms(String perms) {
//        this.perms = perms;
//    }

//    @Override
//    public String toString() {
//        return "Employee{" +
//                "id=" + id +
//                ", lastName='" + lastName + '\'' +
//                ", password='" + password + '\'' +
//                ", email='" + email + '\'' +
//                ", departmentId=" + departmentId +
//                ", perms='" + perms + '\'' +
//                '}';
//    }
}
