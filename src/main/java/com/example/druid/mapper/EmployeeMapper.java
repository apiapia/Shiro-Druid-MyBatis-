package com.example.druid.mapper;

import com.example.druid.bean.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 1.Mapper application.yml 引用 mybatis-config.xml
 * 2.启动Application 引入@MapperScan(value = "com.example.druid.mapper")
 * 3.每个@Mapper文件无须再分别注入;
 * */
//@Mapper
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public Employee getEmpByName(String name);

    //模糊查询返回的结果集
    public List<Employee> selectEmpLikeName(String name);

    public Employee getEmpByNameAndPassword(@Param("lastName") String lastName, @Param("password") String password);
    /**
     * 插入Empolyee，返回主键
     */
    public void insertEmp(Employee employee);

    /**
     * 查询所有员工
     * */
    public List<Employee> getEmps();

}
