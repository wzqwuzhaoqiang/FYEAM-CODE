<%@ page language="java"  contentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"%>
     <%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%> 
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<base href="<%=basePath%>">
  <head>
    <title>前台物品借用</title>
      <script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
       <script type="text/javascript">
    wx.config({
        beta: true,// 必须这么写，否则wx.invoke调用形式的jsapi会有问题
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: '${appId}', // 必填，企业微信的corpID
        timestamp:'${timestamp}', // 必填，生成签名的时间戳
        nonceStr:'${nonceStr}', // 必填，生成签名的随机串
        signature:'${signature}',// 必填，签名，见 附录-JS-SDK使用权限签名算法
        jsApiList:['checkJsApi', 'startRecord', 'stopRecord','translateVoice','scanQRCode', 'openCard'] // 必填，需要使用的JS接口列表，凡是要调用的接口都需要传进来
    });
    
    wx.ready(function(){
    	fsubmit();
    	// config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
    });
    
    wx.error(function(res){
    	alert("扫码出错，地址："+ location.href.split('#')[0]+"，原因："+JSON.stringify(res));
    	console.log("扫码出错，原因："+JSON.stringify(res));
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
    });
    
    
    function fsubmit() {
    	wx.scanQRCode({
            desc: 'scanQRCode desc',
            needResult: 1, // 默认为0，扫描结果由企业微信处理，1则直接返回扫描结果，
            scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是条形码（一维码），默认二者都有
            success: function(res) {
                // 回调
                
            	window.location.href="createBorrowSecondMessage?command="+res.resultStr;
            },
            error: function(res) {
                if (res.errMsg.indexOf('function_not_exist') > 0) {
                    alert('版本过低请升级');
                }
            }
        });
	
	//window.open("http://127.0.0.1:9001/eam/loading","_blank");

    }
    </script>
  </head>
  <body>
  <div id="bj">
     <img   src="images/workorderreport/mask.jpg" ></div>

     <style type="text/css">

    #bj{

    width:900px;

    height:1300px;

    text-align:center;

    vertical-align:middle;

    display:table-cell;  //将对象作为表格单元格显示

    background:pink;

    }

    img{

       width:300px;

       height:300px;

       margin:0 auto;

       vertical-align:middle;
       }

    </style>
  </body>
</html>
