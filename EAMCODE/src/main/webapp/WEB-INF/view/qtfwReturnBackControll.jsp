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
  <title>前台物品归还</title>
      <link href="files/weixin/styles.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="files/weixin/animationOnAndOut.v1.css">
     <link rel="stylesheet" href="files/weixin/makaElementEffect.v2.css">
</head>

<body>
<h1 style="display: none;">归还物品</h1>
<div class="maka-canvas" >
  <div class="maka-page maka-page-0" >
  <div class="page-bg-0" >
<div class="maka-eleCanvas maka-eleCanvas-0" >
<div class="big-editor-new-form rotateZ0" >
<form name="uploadform" id="uploadform" action="/eam/returnBackApprovee" method="POST" enctype="multipart/form-data">
<div class="animated">
<div id="title-pd">归还物品申请</div>
<p></p>
<div class="field-wrapper" ><input  id="tableID" name="tableID" class="input-style" type="hidden" value="${waitrbConfirmObj.tableID}"	/></div>
<div class="field-wrapper" ><label>借用人工号</label><input id="userid" name="userid" class="input-style" type="text" value="${waitrbConfirmObj.borrowerId}" readonly	/></div>
<div class="field-wrapper" ><label>借用人名字</label><input id="userName" name="userName" class="input-style" type="text" value="${waitrbConfirmObj.borrowerName}" readonly/></div>
<div class="field-wrapper" ><label>物品名称</label><input   id="thingName" name="thingName" class="input-style" type="text" value="${waitrbConfirmObj.tools}"	readonly/></div>
<div class="field-wrapper" ><label>数量</label><input      id="count" name="count" class="input-style"  	value="${waitrbConfirmObj.count}"	readonly/></div>
<div class="field-wrapper" ><label>借用时间</label><input   id="borrowTime" name="borrowTime" class="input-style" type="text" value="${waitrbConfirmObj.borrowTime}" readonly	/></div>
<div class="field-wrapper" ><label>归还时间</label><input   id="currentTime" name="currentTime" class="input-style" type="text" value="${waitrbConfirmObj.returnTime}" readonly	/></div>
<div class="field-wrapper" ><label>状态</label><input   id="status" name="backConfirm" class="input-style" type="text" value="${waitrbConfirmObj.backConfirm}" readonly	/></div>
<div class="field-wrapper" ><label>联系方式</label><input   id="mobile" name="mobile" class="input-style" type="text" value="${waitrbConfirmObj.mobile}" readonly	/></div>
<div class="field-wrapper" ><input type="button" class = "verify-button" value="通过申请" onclick="fsubmit('pass')"/></div>
<p></p>

<div class="field-wrapper" ><input type="button" class = "verify-button" value="取消申请" onclick="fsubmit('cancel')"/></div>
</div>
</div>
</form>
<p></p>
</div>
</div>

</div>

</div></div>
        <script type="text/javascript">
        
        
        
        
        var _loging;
        function fsubmit(command) {
        	//alert(imgurl);
        		$.ajax({
                    type: "GET",//方法类型
                    dataType: "json",//预期服务器返回的数据类型
                    url: "/eam/rbcomfirmAction?acname="+command,//url
                    //contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    success: function (result) {
                    	 layer.close(_loging);
                    	//result = $.parseJSON(result.replace(/<.*?>/ig,""));
                    	firm(result.message);
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
        }
        
        function compressImage(url,filename){  
            var name="_doc/upload/"+filename;
            plus.zip.compressImage({  
                    src:url,//src: (String 类型 )压缩转换原始图片的路径  
                    dst:name,//压缩转换目标图片的路径  
                    quality:40,//quality: (Number 类型 )压缩图片的质量.取值范围为1-100  
                    overwrite:true//overwrite: (Boolean 类型 )覆盖生成新文件  
                },  
                function(zip) {
             	   //页面显示图片
             	   showPics(zip.target,name);
                },function(error) {  
                    plus.nativeUI.toast("压缩图片失败，请稍候再试");  
            });  
        }
        
        	function firm(message){
        		confirm(message);
        		WeixinJSBridge.call('closeWindow');
        		    }

</script>
  
    
</body>
<style type="text/css">
    img{
    vertical-align: middle;
    max-height: 65%;
    z-index: 999;
    max-width: 100%;
    display: inline-block;
    }
	html,body{ 
	     width: 100%; 
	     min-width: 480px;
	     height: auto;
	}
</style>
</html>