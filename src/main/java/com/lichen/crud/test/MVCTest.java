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
 * ʹ��spring����ģ���ṩ�Ĳ��������ܣ�����CRUD����
 * @author ���
 * 2019����4:14:41
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:applicationContext.xml","file:src/main/webapp/WEB-INF/dispatcherServlet-servlet.xml"})
public class MVCTest {
	
	//����springmvc��ioc
	@Autowired
	WebApplicationContext context;
	
	//���mvc���󣬻�ȡ���Ľ��
	MockMvc mockMvc;
	
	@Before
	public void initMockMvc(){
		mockMvc=MockMvcBuilders.webAppContextSetup(context).build();	
	}
	
	@Test
	public void testPage() throws Exception{
		//ģ�������õ�����ֵ
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn", "5")).andReturn();
		
		//����ɹ��Ժ��������л���pageinfo��ȡ��pageinfo������֤
		MockHttpServletRequest request = result.getRequest();
		PageInfo pi = (PageInfo) request.getAttribute("pageinfo");
		System.out.println("��ǰҳ�룺"+pi.getPageNum());
		System.out.println("��ҳ������"+pi.getPages());
		System.out.println("�ܼƼ�¼����"+pi.getTotal());
		System.out.println("��ҳ����Ҫ������ʾ��ҳ�룺");
		int[] nums=pi.getNavigatepageNums();
		for (int i : nums) {
			System.out.print(" "+i);
		}
		
		//��ȡԱ������
		List<Employee> list = pi.getList();
		for (Employee employee : list) {
			System.out.println("ID��"+employee.getEmpId()+"===>Name"+employee.getEmpName());
		}
		
	}
	
}
