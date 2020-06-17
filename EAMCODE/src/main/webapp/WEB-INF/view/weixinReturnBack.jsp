<%@ page language="java"  contentType="text/html;charset=utf-8" 
    pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
int currntindex = (int) session.getAttribute("currntPage");
int totalpage = (int) session.getAttribute("total");
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
  <!-- <img class="image-form" src="images/weixin/bg.jpg" ></div> -->
<div class="maka-eleCanvas maka-eleCanvas-0" >
<div class="big-editor-new-form rotateZ0" >
<form name="uploadform" id="uploadform" action="/eam/returnBackApprovee" method="POST" enctype="multipart/form-data">
<div class="animated">
<div id="title-pd">归还物品</div>
<p></p>
<div class="field-wrapper" ><input  id="tableID" name="tableID" class="input-style" type="hidden" value="${windowServer.tableID}"	/></div>
<div class="field-wrapper" ><label>借用人工号</label><input id="userid" name="userid" class="input-style" type="text" value="${windowServer.borrowerId}" readonly	/></div>
<div class="field-wrapper" ><label>借用人名字</label><input id="userName" name="userName" class="input-style" type="text" value="${windowServer.borrowerName}" readonly/></div>
<div class="field-wrapper" ><label>物品名称</label><input   id="thingName" name="thingName" class="input-style" type="text" value="${windowServer.tools}"	readonly/></div>
<div class="field-wrapper" ><label>数量</label><input      id="count" name="count" class="input-style"  	value="${windowServer.count}"	readonly/></div>
<div class="field-wrapper" ><label>借用时间</label><input   id="borrowTime" name="borrowTime" class="input-style" type="text" value="${windowServer.borrowTime}" readonly	/></div>
<div class="field-wrapper" ><label>归还时间</label><input   id="currentTime" name="currentTime" class="input-style" type="text" value="${currentTime}" readonly	/></div>
<div class="field-wrapper" ><label>拍照</label><input type="hidden" id="picIndex" value="0">
<input type="hidden" id="currnt" value="<%=currntindex %>">
<input type="hidden" id="total" value="<%=totalpage %>">
                <div id='image-list' class="row image-list">
                	<input id="upload" name="image"  type="file" accept="image/*" capture="camera"> 
                	 <div class="image-item space" οnclick=" getImage()">
                		<div class="image-up"></div>
                	</div>
<div><label class="label-style2">请仔细核对以上信息，是否和本人一致。</label></div>
<div class="field-wrapper" ><input type="button" class = "verify-button" value="确认" onclick="fsubmit()"/></div>
<p></p>
<div class="field-wrapper" ><input type="button" class = "verify-button" value="下一条" onclick="next()"/></div>
</div>
</form>
<p></p>
</div>
</div>

</div>

</div></div>
        <script type="text/javascript">
        var _loging;
        function fsubmit() {
        	var imgurl = document.getElementById("upload").value;
        	//alert(imgurl);
        	 _loging = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });
        	 $.ajaxFileUpload({
                    type: "POST",//方法类型
                    dataType: "text",//预期服务器返回的数据类型
                    //data:$('#uploadform').serialize(),
                    url: "/eam/returnBackApprove",//url
                    fileElementId: "upload", 
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    processData: false,
                    success: function (result) {
                    	 layer.close(_loging);
                    	result = $.parseJSON(result.replace(/<.*?>/ig,""));
                    	firm(result.message);
                       	//alert(result.message);
                    },
                    error : function(result) {
                    	 layer.close(_loging);
                    	 firm(result.message);
                        //alert("盘点有误，非本人操作。如未进行内部转移，请先填写OA单据：固定资产内部转移登记表。");
                       
                    }
                });
        	
                }
        function next(){
        	var page =parseInt($("#currnt").val()) ;
        	var total =parseInt($("#total").val()) ;
        	//alert("总："+total+",当前："+page+(typeof page));
        	if(page<total-1){
        		$.ajax({
                    type: "get",//方法类型
                    url: "/eam/nextData",//url
                    dataType: "json",
                    success: function (result) {
                    	$("#tableID").val(result.tableID) ;
                    	$("#userid").val(result.userid) ;
                    	$("#userName").val(result.userName) ;
                    	$("#thingName").val(result.thingName) ;
                    	$("#count").val(result.count) ;
                    	$("#borrowTime").val(result.borrowTime) ;
                    	$("#currnt").val(result.currnt) ;
                    },
                    error : function(result) {
                    	 layer.close(_loging);
                    	 firm(result.message);
                        //alert("盘点有误，非本人操作。如未进行内部转移，请先填写OA单据：固定资产内部转移登记表。");
                       
                    }
                });
        	}else{
        		alert("没有更多的记录！");
        	}
        	
        	
        }
        

        	//压缩图片  
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

        	//调用手机摄像头并拍照
        function getImage() {  
            var cmr = plus.camera.getCamera();  
            cmr.captureImage(function(p) {  
                plus.io.resolveLocalFileSystemURL(p, function(entry) {  
                    compressImage(entry.toLocalURL(),entry.name);  
                }, function(e) {  
                    plus.nativeUI.toast("读取拍照文件错误：" + e.message);  
                });  
            }, function(e) {  
            }, {  
                filter: 'image' 
            });  
        }
        //从相册选择照片
        function galleryImgs() {  
        	    plus.gallery.pick(function(e) {  
        	    	var name = e.substr(e.lastIndexOf('/') + 1);
        	       compressImage(e,name);
        	    }, function(e) {  
        	    }, {  
        	        filter: "image"  
        	    });  
        	}

        //点击事件，弹出选择摄像头和相册的选项
        	function showActionSheet() {  
        		 getImage();  
        	   // var bts = [{  
        	   //     title: "拍照"  
        	   // }];  
        	    //plus.nativeUI.actionSheet({  
        	    //        cancel: "取消",  
        	     //       buttons: bts  
        	     //   },  
        	      //  function(e) {  
        	      //          getImage();  
        	      //  }  
        	   // );  
        	}
        	
        	function firm(message){
        		if(confirm(message+",是否退出吗？")){ 
        			
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