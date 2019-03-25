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
 * @author 李琛
 * 2019下午3:46:56
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
	 * 检验用户名是否可用
	 * @param empName
	 * @return true:0 代表当前姓名可用 false：不可用
	 */
	public boolean checkUser(String empName) {
		EmployeeExample example=new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpNameEqualTo(empName);
		long count=employeeMapper.countByExample(example);	
		return count == 0;
	}

	/**
	 * 按照员工id查询员工
	 * @param empId
	 * @return
	 */
	public Employee getEmp(Integer empId) {
		Employee employee = employeeMapper.selectByPrimaryKey(empId);
		return employee;
	}

	/**
	 * 员工更新
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
