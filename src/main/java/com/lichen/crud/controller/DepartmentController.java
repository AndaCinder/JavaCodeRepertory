package com.lichen.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lichen.crud.beans.Department;
import com.lichen.crud.beans.Msg;
import com.lichen.crud.service.DepartmentService;
/**
 * 处理和部门有关的请求
 * @author 李琛
 * 2019下午2:53:21
 */
@Controller
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService; 
	
	/**
	 * 查出所有的部门信息
	 */
	@RequestMapping("/depts")
	@ResponseBody
	public Msg getDepts(){
		//查出所有部门信息
		List<Department> deptsList=departmentService.getDepts();
		
		return Msg.success().add("depts", deptsList);
	}
	
}
