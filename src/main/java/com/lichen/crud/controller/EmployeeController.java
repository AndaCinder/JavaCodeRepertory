package com.lichen.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichen.crud.beans.Employee;
import com.lichen.crud.beans.Msg;
import com.lichen.crud.service.EmployeeService;

/**
 * 处理CRUD请求
 * @author 李琛
 * 2019下午3:45:17
 */
@Controller
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	/**
	 * 单个批量二合一
	 * 批量删除1-2-3
	 * 单个删除1
	 * 
	 * @param empId
	 * @return
	 */
	@RequestMapping(value="/emp/{ids}",method=RequestMethod.DELETE)
	@ResponseBody
	public Msg deleteEmpById(@PathVariable("ids")String ids){
		if (ids.contains("-")) {
			List<Integer> del_ids=new ArrayList<Integer>();
			String[] str_ids = ids.split("-");
			//组装id的集合
			for (String string : str_ids) {
				del_ids.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(del_ids);
		}else {
			Integer empId=Integer.parseInt(ids);
			employeeService.deleteEmp(empId);
		}
		
		return Msg.success();
	}
	
	
	
	/**
	 * 如果直接发送Ajax=PUT的形式的请求
	 * 
	 * 问题：
	 * 请求体中有数据，但是employee封装不上
	 * 员工更新方法
	 * 
	 * 原因：
	 * tomcat：
	 * 		1.将请求体中的数据，封装一个map
	 * 		2.request.getParameter("empName")会从这个map中取值
	 * 		3.springMVC封装POJO对象会把POJO中每个属性的值：request.getParameter
	 * 
	 * AJAX发送PUT请求引发的惨痛教训
	 * 			PUT请求：请求体中的数据：request.getParameter拿不到
	 * 			Tomcat发送PUT请求不会封装请求体中的数据为map，只有POST形式的请求才封装请求体为map
	 * 解决方法呢：
	 * 我们要能支持直接发送PUT之类的请求，还要封装请求体中的数据
	 * 配置HttpPutFormContentFilter
	 * 作用：将请求体中的数据解析包装成一个map，request被重新包装，
	 * request.getParameter()被重写，就会从自己封装的map中取数据
	 * 
	 * @param employee
	 * @return
	 */
	@RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
	@ResponseBody
	public Msg updateEmp(Employee employee){
		employeeService.updateEmp(employee);
		return Msg.success();
	}
	
	
	/**
	 * 查询员工数据回显修改表单
	 * @param empId
	 * @return
	 */
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer empId){
		
		Employee employee=employeeService.getEmp(empId);
		return Msg.success().add("emp", employee);
	}
	
	
	/**
	 * 检查用户名是否可用
	 * 	注释：奇怪现象，将@ResponsBody注解放在@RequestMapping上面则不会发送checkuser请求，或者说接收不到前端ajax请求
	 * @param empName
	 * @return
	 */
	@RequestMapping(value="/checkuser",method=RequestMethod.POST)
	@ResponseBody
	public Msg checkUser(@RequestParam("empName")String empName){
		//先判断用户名是否是合法的表达式
		String regex="(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5}$)";
		if(!empName.matches(regex)){
			return Msg.fail().add("va_msg", "用户名为2-5位中文或6-16位英文和数字的组合");
		}
		//数据库用户名重复校验
		boolean b=employeeService.checkUser(empName);
		if (b) {
			return Msg.success();
		}else {
			return Msg.fail().add("va_msg", "用户名存在");
		}
	
	}
	
	/**
	 * 员工保存
	 * 1.支持JSR303校验
	 * 2.导入Hibernate-validator
	 * @return
	 */
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee,BindingResult result){
		if (result.hasErrors()) {
			//校验失败，在模态框中显示错误失败信息
			Map<String, Object> map=new HashMap<String, Object>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println("错误的字段名:"+fieldError.getField());
				System.out.println("错误信息:"+fieldError.getDefaultMessage());
				map.put(fieldError.getField(),fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
		}else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
		
	}
	
	/**
	 * 需要导入Jackson包
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value="pn",defaultValue="1")Integer pn,Model model){
		PageHelper.startPage(pn, 5);
		//startpage后面紧跟的的这个查询就是一个分页查询
		List<Employee> emps=employeeService.getAll();
		//使用pageinfo包装查询后的结果,只需要将pagheinfo交给页面就完事了
		//封装了详细的信息，包括有我们查询出来的数据,可以传入连续显示的页数
		PageInfo page = new PageInfo(emps,5);
		return Msg.success().add("pageInfo", page);
	}
	
	
	/**
	 * 查询员工数据(分页查询)
	 * @return
	 */
	//@RequestMapping("/emps")
	public String getEmps(@RequestParam(value="pn",defaultValue="1")Integer pn,Model model){
		//然而没有分页查询；
		//所以引入pageHelper分页插件
		//在查询之前只需要调用，传入页码，以及每页的大小
		PageHelper.startPage(pn, 5);
		//startpage后面紧跟的的这个查询就是一个分页查询
		List<Employee> emps=employeeService.getAll();
		//使用pageinfo包装查询后的结果,只需要将pagheinfo交给页面就完事了
		//封装了详细的信息，包括有我们查询出来的数据,可以传入连续显示的页数
		PageInfo page = new PageInfo(emps,5);
		model.addAttribute("pageInfo", page);
		return  "list";
	}
}
