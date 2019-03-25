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
 * ����dao��Ĺ���
 * @author ���
 * 2019����10:46:55
 * �Ƽ�spring����Ŀ�Ϳ���ʹ��spring�ĵ�Ԫ���ԣ������Զ�ע��������Ҫ�����
 * 1.����springTestģ��
 * 2.@ContextConfigurationָ��spring�����ļ���λ��
 * 3.ֱ��autowireҪʹ�õ�������� 
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
	 * ����DepartmentMapper
	 */
	@Test
	public void testCRUD01(){
		System.out.println(departmentMapper);
		
		//1.���뼸������
		//departmentMapper.insertSelective(new Department(null, "��潲�"));
		//departmentMapper.insertSelective(new Department(null, "ɨ�Ʋ�"));
		
		//2.����һ��Ա����Ϣ
		//employeeMapper.insertSelective(new Employee(null, "��潿���", "��", "������ĺ��Ѽǵ�", 1));
		
		//3.����������Ա����������ʹ�ÿ���ִ������������SQLSession
		EmployeeMapper mapper = sqlsession.getMapper(EmployeeMapper.class);
		for(int i=0;i<100;i++){
			String uid = UUID.randomUUID().toString().substring(0, 5);
			employeeMapper.insertSelective(new Employee(null, uid, "Ů", uid+"@yaomingTV.com", 2));
		}
		System.out.println("������");
		
		
		
	}
}
