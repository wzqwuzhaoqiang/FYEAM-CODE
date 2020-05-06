<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'addBat.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
   <form action="/eam/addBat" method="post">
   <table>
   <tr>增加盘点批次</tr>
   <tr><th>批次编号<th><th><input type="text" name="pdBatCode" /></th></tr>
    <tr><th>盘点开始时间<th><th><input type="date" name="pdStartDate" /></th></tr>
     <tr><th>盘点结束时间<th><th><input  type="date" name="pdEndDate" /></th></tr>
      <tr><th>*是否全体<th>
     <th> <input list="isAll" name="isAll">
		<datalist id="isAll">
  		<option value="是">
 		 <option value="否">
		</datalist></th>
		</tr>
      <tr><th>盘点公司（以分号隔开）<th><th><input type="text" name="pdBatCode" /></tr>
      <tr><th><input type="submit" id="submit" name="submit_button" value="提交" /></th></tr>
      </table>
   </form>
  </body>
</html>
