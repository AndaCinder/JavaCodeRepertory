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
 * ����CRUD����
 * @author ���
 * 2019����3:45:17
 */
@Controller
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	/**
	 * ������������һ
	 * ����ɾ��1-2-3
	 * ����ɾ��1
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
			//��װid�ļ���
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
	 * ���ֱ�ӷ���Ajax=PUT����ʽ������
	 * 
	 * ���⣺
	 * �������������ݣ�����employee��װ����
	 * Ա�����·���
	 * 
	 * ԭ��
	 * tomcat��
	 * 		1.���������е����ݣ���װһ��map
	 * 		2.request.getParameter("empName")������map��ȡֵ
	 * 		3.springMVC��װPOJO������POJO��ÿ�����Ե�ֵ��request.getParameter
	 * 
	 * AJAX����PUT���������Ĳ�ʹ��ѵ
	 * 			PUT�����������е����ݣ�request.getParameter�ò���
	 * 			Tomcat����PUT���󲻻��װ�������е�����Ϊmap��ֻ��POST��ʽ������ŷ�װ������Ϊmap
	 * ��������أ�
	 * ����Ҫ��֧��ֱ�ӷ���PUT֮������󣬻�Ҫ��װ�������е�����
	 * ����HttpPutFormContentFilter
	 * ���ã����������е����ݽ�����װ��һ��map��request�����°�װ��
	 * request.getParameter()����д���ͻ���Լ���װ��map��ȡ����
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
	 * ��ѯԱ�����ݻ����޸ı�
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
	 * ����û����Ƿ����
	 * 	ע�ͣ�������󣬽�@ResponsBodyע�����@RequestMapping�����򲻻ᷢ��checkuser���󣬻���˵���ղ���ǰ��ajax����
	 * @param empName
	 * @return
	 */
	@RequestMapping(value="/checkuser",method=RequestMethod.POST)
	@ResponseBody
	public Msg checkUser(@RequestParam("empName")String empName){
		//���ж��û����Ƿ��ǺϷ��ı��ʽ
		String regex="(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5}$)";
		if(!empName.matches(regex)){
			return Msg.fail().add("va_msg", "�û���Ϊ2-5λ���Ļ�6-16λӢ�ĺ����ֵ����");
		}
		//���ݿ��û����ظ�У��
		boolean b=employeeService.checkUser(empName);
		if (b) {
			return Msg.success();
		}else {
			return Msg.fail().add("va_msg", "�û�������");
		}
	
	}
	
	/**
	 * Ա������
	 * 1.֧��JSR303У��
	 * 2.����Hibernate-validator
	 * @return
	 */
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee,BindingResult result){
		if (result.hasErrors()) {
			//У��ʧ�ܣ���ģ̬������ʾ����ʧ����Ϣ
			Map<String, Object> map=new HashMap<String, Object>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println("������ֶ���:"+fieldError.getField());
				System.out.println("������Ϣ:"+fieldError.getDefaultMessage());
				map.put(fieldError.getField(),fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
		}else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
		
	}
	
	/**
	 * ��Ҫ����Jackson��
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value="pn",defaultValue="1")Integer pn,Model model){
		PageHelper.startPage(pn, 5);
		//startpage��������ĵ������ѯ����һ����ҳ��ѯ
		List<Employee> emps=employeeService.getAll();
		//ʹ��pageinfo��װ��ѯ��Ľ��,ֻ��Ҫ��pagheinfo����ҳ���������
		//��װ����ϸ����Ϣ�����������ǲ�ѯ����������,���Դ���������ʾ��ҳ��
		PageInfo page = new PageInfo(emps,5);
		return Msg.success().add("pageInfo", page);
	}
	
	
	/**
	 * ��ѯԱ������(��ҳ��ѯ)
	 * @return
	 */
	//@RequestMapping("/emps")
	public String getEmps(@RequestParam(value="pn",defaultValue="1")Integer pn,Model model){
		//Ȼ��û�з�ҳ��ѯ��
		//��������pageHelper��ҳ���
		//�ڲ�ѯ֮ǰֻ��Ҫ���ã�����ҳ�룬�Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		//startpage��������ĵ������ѯ����һ����ҳ��ѯ
		List<Employee> emps=employeeService.getAll();
		//ʹ��pageinfo��װ��ѯ��Ľ��,ֻ��Ҫ��pagheinfo����ҳ���������
		//��װ����ϸ����Ϣ�����������ǲ�ѯ����������,���Դ���������ʾ��ҳ��
		PageInfo page = new PageInfo(emps,5);
		model.addAttribute("pageInfo", page);
		return  "list";
	}
}
