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
  <title>固定资产盘点</title>
      <link href="files/weixin/styles.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="files/weixin/animationOnAndOut.v1.css">
     <link rel="stylesheet" href="files/weixin/makaElementEffect.v2.css">
</head>

<body>
<h1 style="display: none;">固定资产盘点</h1>
<div class="maka-canvas" >
  <div class="maka-page maka-page-0" >
  <div class="page-bg-0" >
  <!-- <img class="image-form" src="images/weixin/bg.jpg" ></div> -->
<div class="maka-eleCanvas maka-eleCanvas-0" >
<div class="big-editor-new-form rotateZ0" >
<form name="uploadform" action="/eam/eamApprove" method="POST" enctype="multipart/form-data">
<div class="animated">
<div id="title-pd">固定资产盘点</div>
<p></p>
<div class="field-wrapper" ><label>序列号</label><input  class="input-style" type="text" value=${asset.serialNumber} readonly	/></div>
<div class="field-wrapper" ><label>资产名称</label><input  class="input-style" type="text" value=${asset.description} readonly	/></div>
<div class="field-wrapper" ><label>使用部门</label><input  class="input-style" type="text" value=${asset.workCenterName} readonly	/></div>
<div class="field-wrapper" ><label>使用人</label><input  class="input-style" type="text" value=${asset.jobnum} readonly	/></div>
<div class="field-wrapper" ><label>使用人名字</label><input  class="input-style" type="text" value=${asset.username} readonly	/></div>
<div class="field-wrapper" ><label>基础配置</label><input  class="input-style" type="text"  value='${asset.allocation}' readonly	/></div>
<div class="field-wrapper" ><label>拍照</label><input type="hidden" id="picIndex" value="0">
                <div id='image-list' class="row image-list">
                	<input id="upload" name="image"  type="file" accept="image/*" capture="camera"> 
                	 <div class="image-item space" οnclick=" getImage()">
                		<div class="image-up"></div>
                	</div>
<div><label class="label-style2">请仔细核对以上资产信息，是否和所使用的资产一致。</label></div>
<div class="field-wrapper" ><input type="button" class = "verify-button" value="确认" onclick="fsubmit()"/></div>
<div><label class="label-style2">信息不一致无法盘点的，请联系信息部叶修龙(18811579184，xiulong.ye@fuyaogroup.com)</label></div></div>
</div></div>
</form>

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
            url: "/eam/eamApprove",//url
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