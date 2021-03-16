<%@ page language="java"  contentType="text/html;charset=utf-8" 
    pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
int wsListmaxSize = (int)session.getAttribute("wsListmaxSize");
int wsListstart= (int)session.getAttribute("wsListstart");
int wsListend= (int)session.getAttribute("wsListend");
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
  <title>归还列表</title>
      <link href="files/weixin/styles.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="files/weixin/animationOnAndOut.v1.css">
     <link rel="stylesheet" href="files/weixin/makaElementEffect.v2.css">
</head>

<body>
<table width="100%">
                <tr>
                    <td width="10%">操作        </td>
                    <td width="10%">操作        </td>
                    <td width="10%">操作        </td>
                    <td width="10%">操作        </td>
                    <td width="10%">归还人工号        </td>
                    <td width="10%">归还人姓名        </td>
                    <td width="20%">归还物品        </td>
                    <td width="10%">申请时间        </td>
                </tr>
                
<c:forEach items="${wsList}" var="obj" begin="<%=wsListstart %>" end="<%=wsListend %>">
<br><div style="position:relative; width:600; height:1px; background-color:break;"></div></br>
                 <tr>
                 <td width="10%"><button value="${obj.tableID}" onclick="rebackSQ(this.value)">接收</button></td>
                 <td width="10%"><button value="${obj.tableID}" onclick="rebackSH(this.value,'${obj.tools}')">损坏</button></td>
                 <td width="10%"><button value="${obj.tableID}" onclick="rebackDS(this.value,'${obj.tools}')">丢失</button></td>
                 <td width="10%"><button value="${obj.tableID}" onclick="rebackBF(this.value,'${obj.tools}')">报废</button></td>
                     <td width="20%">${obj.borrowerId}</td>
                     <td width="20%">${obj.borrowerName}</td>
                     <td width="20%">${obj.tools}</td>
                     <td width="20%">${obj.borrowTime}</td>
                 </tr>
</c:forEach>
</table>
<p></p>
<p></p>
<div class="jz">
<a href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc625758e50b5ced1&redirect_uri=http%3a%2f%2feam.fuyaogroup.com%2feam%2ftoAddShowReturn&response_type=code&scope=snsapi_base&state=#wechat_redirect">上一页</a>&nbsp;&nbsp;&nbsp;<a href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc625758e50b5ced1&redirect_uri=http%3a%2f%2feam.fuyaogroup.com%2feam%2ftoDetShowReturn&response_type=code&scope=snsapi_base&state=#wechat_redirect">下一页</a>
</div>
    <script type="text/javascript">
    
    function rebackSH(tableID,tools) {
    	if (confirm("请再次确认"+tools+"是否损坏")==true){ 
    		$.ajax({
                type: "GET",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: "/eam/rbcomfirmAction?acname=sh&idvalue="+tableID,//url
                //contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (result) {
                	 //layer.close(_loging);
                	//result = $.parseJSON(result.replace(/<.*?>/ig,""));
                	firm(result.message);
                	//turnback = "success";
                   	//alert(result.message);
                },
                error : function(result) {
                	 //layer.close(_loging);
                	 firm(result.message);
                    //alert("盘点有误，非本人操作。如未进行内部转移，请先填写OA单据：固定资产内部转移登记表。");
                	 //turnback = "false";
                }
            });
    		 }else{ 
    		  return false; 
    		 } 
    	
	}
    
    
    
    function rebackDS(tableID,tools) {
    	if (confirm("请再次确认"+tools+"是否丢失")==true){ 
    		$.ajax({
                type: "GET",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: "/eam/rbcomfirmAction?acname=ds&idvalue="+tableID,//url
                //contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (result) {
                	 //layer.close(_loging);
                	//result = $.parseJSON(result.replace(/<.*?>/ig,""));
                	firm(result.message);
                	//turnback = "success";
                   	//alert(result.message);
                },
                error : function(result) {
                	 //layer.close(_loging);
                	 firm(result.message);
                    //alert("盘点有误，非本人操作。如未进行内部转移，请先填写OA单据：固定资产内部转移登记表。");
                	 //turnback = "false";
                }
            });
    		 }else{ 
    		  return false; 
    		 } 
    	
	}
    
    
    function rebackBF(tableID,tools) {
    	if (confirm("请再次确认"+tools+"是否报废")==true){ 
    		$.ajax({
                type: "GET",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: "/eam/rbcomfirmAction?acname=bf&idvalue="+tableID,//url
                //contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (result) {
                	 //layer.close(_loging);
                	//result = $.parseJSON(result.replace(/<.*?>/ig,""));
                	firm(result.message);
                	//turnback = "success";
                   	//alert(result.message);
                },
                error : function(result) {
                	 //layer.close(_loging);
                	 firm(result.message);
                    //alert("盘点有误，非本人操作。如未进行内部转移，请先填写OA单据：固定资产内部转移登记表。");
                	 //turnback = "false";
                }
            });
    		 }else{ 
    		  return false; 
    		 } 
    	
	}
    
    
    
    
    
    
    
    function rebackSQ(tableID) {
    	window.location.href="rbcontroll?idvalue="+tableID;
	}
    function firm(message){
		confirm(message);
		WeixinJSBridge.call('closeWindow');
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
 