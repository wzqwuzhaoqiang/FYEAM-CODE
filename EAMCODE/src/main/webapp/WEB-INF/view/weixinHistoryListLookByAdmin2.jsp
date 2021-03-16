<%@ page language="java"  contentType="text/html;charset=utf-8" 
    pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
int maxSize = (int)session.getAttribute("hmaxSize");
int start= (int)session.getAttribute("hstart");
int end= (int)session.getAttribute("hend");
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
  <title>历史记录</title>
      <link href="files/weixin/styles.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="files/weixin/animationOnAndOut.v1.css">
     <link rel="stylesheet" href="files/weixin/makaElementEffect.v2.css">
</head>

<body>
<table width="100%">
                <tr>
                    <td width="20%">借用人姓名        </td>
                    <td width="20%">借用物品        </td>
                    <td width="20%">借用时间        </td>
                    <td width="20%">借用人工号        </td>
                    <td width="20%">联系方式        </td>
                    <td width="20%">状态        </td>
                </tr>
                
<c:forEach items="${hlistSize}" var="obj" begin="<%=start %>" end="<%=end %>">
<br><div style="position:relative; width:600; height:1px; background-color:break;"></div></br>
                 <tr>
                     <td width="20%">${obj.borrowerName}</td>
                     <td width="20%">${obj.tools}</td>
                     <td width="20%">${obj.borrowTime}</td>
                     <td width="20%">${obj.borrowerId}</td>
                     <td width="20%">${obj.mobile}</td>
                     <td width="20%">${obj.status}</td>
                 </tr>
</c:forEach>
</table>
<p></p>
<p></p>
<div class="jz">
<a href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc625758e50b5ced1&redirect_uri=http%3a%2f%2feam.fuyaogroup.com%2feam%2ftoAddShowHistory&response_type=code&scope=snsapi_base&state=#wechat_redirect">上一页</a>&nbsp;&nbsp;&nbsp;<a href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc625758e50b5ced1&redirect_uri=http%3a%2f%2feam.fuyaogroup.com%2feam%2ftoDetShowHistory&response_type=code&scope=snsapi_base&state=#wechat_redirect">下一页</a>
</div>
    <script type="text/javascript">

    function rebackSQ(tableID) {
    	alert("归还提醒信息发送成功!");
    	window.location.href="hurrybgcontroll?idvalue="+tableID;
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
.jz{

    margin: auto;
   
}

    </style>
</body>

</html>
 