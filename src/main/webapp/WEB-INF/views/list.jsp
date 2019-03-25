<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	pageContext.setAttribute("APP_PATH",request.getContextPath());
 %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>奇怪公司员工列表</title>
<!-- web路径
	不以/开始的相对路径，找资源以当前资源为基准，经常容易出问题
	以/开始的相对路径，找资源以服务器为标准；(http://localhost:8080)需要加上项目名
			http://localhost:8080/crud
 -->
<link href="${APP_PATH}/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
<script src="${APP_PATH}/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</head>
<body>
	<!-- 搭建显示页面 -->
	<div class="container-fluid">
	  <!-- 标题 -->
	  <div class="row">
	  	<div class="col-md-12">
	  		<h1>精神病院</h1>
	  	</div>
	  </div>
	  <!-- 按钮 -->
	  <div class="row">
	  	<div class="col-md-4 col-md-offset-8">
	  		<button type="button" class="btn btn-primary">
	  		<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
	  		新增</button>
	  		<button type="button" class="btn btn-danger">删除</button>
	  	</div>
	  </div>
	  <!-- 显示表格数据 -->
	  <div class="row">
	  	<div class="col-md-12">
	  		<table class="table table-hover">
	  			<tr>
	  				<th>#</th>
	  				<th>empName</th>
	  				<th>gender</th>
	  				<th>email</th>
	  				<th>deptName</th>
	  				<th>操作</th>
	  			</tr>
	  			<c:forEach items="${pageInfo.list}" var="emp">
	  				<tr>
			  			<th>${emp.empId} </th>
						<th>${emp.empName}<th>
						<th>${emp.gender } </th>
						<th>${emp.email } </th>
						<th>${emp.department.deptName}</th>
						<td>
					 	<button class="btn btn-primary btn-sm">
					 	<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
					 	编辑
					 	</button>
					 	<button class="btn btn-danger btn-sm">
					 	<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
					 	删除
					 	</button>
					 </td>
	  			</tr>
	  			</c:forEach>
	  			
	  		</table>
	  	</div>
	  </div>
	  <!-- 显示分页信息 -->
	  <div class="row">
	   	<!-- 分页文字信息 -->
	   	<div class="col-md-6">
	   		当前${pageInfo.pageNum }页，总${pageInfo.pages }页,总${pageInfo.total }条记录
	   	</div>
	   	<!-- 分页条信息 -->
	   	<div class="col-md-6">
	   		<nav aria-label="Page navigation">
			  <ul class="pagination">
			  <li><a href="${APP_PATH }/emps?pn=1">首页</a></li>
			  <c:if test="${pageInfo.hasPreviousPage }">
			  	<li>
			      <a href="${APP_PATH }/emps?pn=${pageInfo.pageNum-1}" aria-label="Previous">
			        <span aria-hidden="true">&laquo;</span>
			      </a>
			    </li>
			  </c:if>
			    
			    <c:forEach items="${pageInfo.navigatepageNums }" var="page_num">
			    	<c:if test="${page_num==pageInfo.pageNum }">
			    		<li class="active"><a href="#">${page_num }</a></li>
			    	</c:if>
			    	<c:if test="${page_num != pageInfo.pageNum }">
			    		<li><a href="${APP_PATH }/emps?pn=${page_num }">${page_num }</a></li>
			    	</c:if>
			    </c:forEach>
			   <c:if test="${pageInfo.hasNextPage }">
			   	<li>
			      <a href="${APP_PATH }/emps?pn=${pageInfo.pageNum+1}" aria-label="Next">
			        <span aria-hidden="true">&raquo;</span>
			      </a>
			    </li>
			   </c:if>
			    
			    <li><a href="${APP_PATH }/emps?pn=${pageInfo.pages }">末页</a></li>
			  </ul>
			</nav>
	   	</div>
	  </div>
	</div>


</body>
</html>