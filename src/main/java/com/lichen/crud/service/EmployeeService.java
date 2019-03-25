package com.lichen.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lichen.crud.beans.Employee;
import com.lichen.crud.beans.EmployeeExample;
import com.lichen.crud.beans.EmployeeExample.Criteria;
import com.lichen.crud.dao.EmployeeMapper;

/**
 * 
 * @author ���
 * 2019����3:46:56
 */
@Service
public class EmployeeService {

	@Autowired
	EmployeeMapper employeeMapper;
	
	public List<Employee> getAll(){
		
		return employeeMapper.selectByExampleWithDept(null);
	}

	public void saveEmp(Employee employee) {
		employeeMapper.insertSelective(employee);
	}

	/**
	 * �����û����Ƿ����
	 * @param empName
	 * @return true:0 ����ǰ�������� false��������
	 */
	public boolean checkUser(String empName) {
		EmployeeExample example=new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpNameEqualTo(empName);
		long count=employeeMapper.countByExample(example);	
		return count == 0;
	}

	/**
	 * ����Ա��id��ѯԱ��
	 * @param empId
	 * @return
	 */
	public Employee getEmp(Integer empId) {
		Employee employee = employeeMapper.selectByPrimaryKey(empId);
		return employee;
	}

	/**
	 * Ա������
	 * @param employee
	 */
	public void updateEmp(Employee employee) {
		employeeMapper.updateByPrimaryKeySelective(employee);
	}

	public void deleteEmp(Integer empId) {
		employeeMapper.deleteByPrimaryKey(empId);
	}

	public void deleteBatch(List<Integer> ids) {
		EmployeeExample example=new EmployeeExample();
		Criteria criteria = example.createCriteria();
		//delete from XXX where emp_id in(1,2,3) 
		criteria.andEmpIdIn(ids);
		employeeMapper.deleteByExample(example);
		
	}

	

}
