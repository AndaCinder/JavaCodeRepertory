package com.lichen.crud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.pagehelper.PageInfo;
import com.lichen.crud.beans.Employee;

/**
 * 使用spring测试模块提供的测试请求功能，测试CRUD请求
 * @author 李琛
 * 2019下午4:14:41
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:applicationContext.xml","file:src/main/webapp/WEB-INF/dispatcherServlet-servlet.xml"})
public class MVCTest {
	
	//传入springmvc的ioc
	@Autowired
	WebApplicationContext context;
	
	//虚假mvc请求，获取到的结果
	MockMvc mockMvc;
	
	@Before
	public void initMockMvc(){
		mockMvc=MockMvcBuilders.webAppContextSetup(context).build();	
	}
	
	@Test
	public void testPage() throws Exception{
		//模拟请求拿到返回值
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn", "5")).andReturn();
		
		//请求成功以后，请求域中会有pageinfo，取出pageinfo进行验证
		MockHttpServletRequest request = result.getRequest();
		PageInfo pi = (PageInfo) request.getAttribute("pageinfo");
		System.out.println("当前页码："+pi.getPageNum());
		System.out.println("总页码数："+pi.getPages());
		System.out.println("总计记录数："+pi.getTotal());
		System.out.println("在页面需要连续显示的页码：");
		int[] nums=pi.getNavigatepageNums();
		for (int i : nums) {
			System.out.print(" "+i);
		}
		
		//获取员工数据
		List<Employee> list = pi.getList();
		for (Employee employee : list) {
			System.out.println("ID："+employee.getEmpId()+"===>Name"+employee.getEmpName());
		}
		
	}
	
}
