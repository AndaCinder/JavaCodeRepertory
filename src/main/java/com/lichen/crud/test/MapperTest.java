package com.lichen.crud.test;

import java.util.UUID;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lichen.crud.beans.Department;
import com.lichen.crud.beans.Employee;
import com.lichen.crud.dao.DepartmentMapper;
import com.lichen.crud.dao.EmployeeMapper;

/**
 * 测试dao层的工作
 * @author 李琛
 * 2019上午10:46:55
 * 推荐spring的项目就可以使用spring的单元测试，可以自动注入我们需要的组件
 * 1.导入springTest模块
 * 2.@ContextConfiguration指定spring配置文件的位置
 * 3.直接autowire要使用的组件即可 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class MapperTest {

	@Autowired
	DepartmentMapper departmentMapper;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	SqlSession sqlsession;
	
	/**
	 * 测试DepartmentMapper
	 */
	@Test
	public void testCRUD01(){
		System.out.println(departmentMapper);
		
		//1.插入几个部门
		//departmentMapper.insertSelective(new Department(null, "嫖娼部"));
		//departmentMapper.insertSelective(new Department(null, "扫黄部"));
		
		//2.生成一个员工信息
		//employeeMapper.insertSelective(new Employee(null, "嫖娼克星", "男", "邮箱真的很难记的", 1));
		
		//3.批量插入多个员工：批量，使用可以执行批量操作的SQLSession
		EmployeeMapper mapper = sqlsession.getMapper(EmployeeMapper.class);
		for(int i=0;i<100;i++){
			String uid = UUID.randomUUID().toString().substring(0, 5);
			employeeMapper.insertSelective(new Employee(null, uid, "女", uid+"@yaomingTV.com", 2));
		}
		System.out.println("搞完了");
		
		
		
	}
}
