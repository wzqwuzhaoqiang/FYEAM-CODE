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
  <title>前台物品借用</title>
      <link href="files/weixin/styles.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="files/weixin/animationOnAndOut.v1.css">
     <link rel="stylesheet" href="files/weixin/makaElementEffect.v2.css">
</head>

<body>
<h1 style="display: none;">物品借用</h1>
<div class="maka-canvas" >
  <div class="maka-page maka-page-0" >
  <div class="page-bg-0" >
  <!-- <img class="image-form" src="images/weixin/bg.jpg" ></div> -->
<div class="maka-eleCanvas maka-eleCanvas-0" >
<div class="big-editor-new-form rotateZ0" >
<form name="uploadform" id="uploadform" action="/eam/borrowApprovee" method="POST">
	 <div class="animated">
	<div id="title-pd">物品借用</div>
	<p></p>
	<div class="field-wrapper" ><label>借用人工号</label><input   class="input-style" type="text" name="userid" value="${userid}"  readonly	/></div>
	<div class="field-wrapper" ><label>借用人名字</label><input   class="input-style" type="text" name="userName" value="${userName}"  readonly	/></div>
	<div class="field-wrapper" ><label>联系方式</label><input  id="mobile"  class="input-style" type="text" name="mobile" value="${mobile}"  readonly	/></div>
	<div class="field-wrapper" ><input type="button" class = "verify-button" value="扫描物品码" onclick="fsubmit()"/></div>
	</div>
	</form>
	</div>
	</div>


</div>

</div>
</div>
    <script type="text/javascript">
    var _loging;
function fsubmit() {
	 $.ajax({
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            data:$('#uploadform').serialize(),
            url: "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc625758e50b5ced1&redirect_uri=http%3a%2f%2fwxtest.fuyaogroup.com%3a8888%2feam%2ftoBorrowSecondPage&response_type=code&scope=snsapi_base&state=#wechat_redirect",//url
            //contentType: "application/x-www-form-urlencoded; charset=utf-8",
            success: function (result) {
            	 //layer.close(_loging);
            	//result = $.parseJSON(result.replace(/<.*?>/ig,""));
            	//firm(result.message);
            	//turnback = "success";
               	//alert(result.message);
            },
            error : function(result) {
            	 layer.close(_loging);
            	 firm(result.message);
                //alert("盘点有误，非本人操作。如未进行内部转移，请先填写OA单据：固定资产内部转移登记表。");
            	 //turnback = "false";
            }
        });
//	 if(turnback=="success"){
//		 alert("借用成功");
//		 window.close();
//	 }else{
//		 alert("借用失败，请联系管理员进行处理");
//	 }
	 
	 
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