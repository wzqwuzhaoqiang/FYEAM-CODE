<%@ page language="java"  contentType="text/html;charset=utf-8" 
    pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%> 
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DtdHTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<base href="<%=basePath%>">
<head>
  <meta charset="utf-8">
  <meta name="format-detection" content="telephone=no"/>
  <meta content="yes" name="apple-mobile-web-app-capable">
  <meta content="black" name="apple-mobile-web-app-status-bar-style">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
    <meta http-equiv="Cache-Control" content="no-cache"/>  
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes,target-densitydpi=device-dpi "> 
  
   <!-- <meta name="viewport"  content="width=320,maximum-scale=1,user-scalable=no">/>  -->
   <script src="resources/scripts/jquery-3.2.1.min.js"></script>
   <script src="resources/scripts/axure/jquery.nicescroll.min.js"></script>
   <script src="data/3.1.1/layer.js"></script>
<script type= "text/javascript" src= "data/ajaxfileupload.js" ></script>
  <title>在借列表</title>
      <link href="files/weixin/styles.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="files/weixin/animationOnAndOut.v1.css">
     <link rel="stylesheet" href="files/weixin/makaElementEffect.v2.css">
</head>

<body>
<table cellpadding="0" cellspacing="0" style="margin: 0 auto" width="100%">
                <tr>
                    <td width="10%">操作        </td>
                    <td width="10%">借用物品        </td>
                   <!--   <td width="10%">数量        </td>-->
                    <td width="10%">状态        </td>
                    
                </tr>
<c:forEach items="${inBorrowList}" var="obj">
                 <tr>
                     <td width="10%"><button value="${obj.serial}" onclick="rebackSQ(this.value)">申请归还</button></td>
                     <td width="10%">${obj.tools}</td>
                   <!--   <td width="10%">${obj.count}</td>-->
                     <td width="10%">${obj.status}</td>
                     
                 </tr>
</c:forEach>
</table>
    <script type="text/javascript">

    //var _loging;
    function rebackSQ(serial) {
    	//var serial = "FYJT-DN-001";
    	window.location.href="createReturnBackMessage?command="+serial;
		
	}
    function firm(message){
    	if(confirm(message+",是否退出？")){ 
    		WeixinJSBridge.call('closeWindow');
    }

    	    }
</script>
  <style type="text/css">
    img {
    vertical-align: middle;
    max-height: 100%;
    z-index: 999;
    max-width: 100%;
    display: inline-block;
}
html,body { 
     width: 100%; 
     min-width: 480px;
     height: auto;
}

    </style>
</body>

</html>
 