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
  <head>
    <title>workOrderReport</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <link href="resources/css/axure_rp_page.css" type="text/css" rel="stylesheet"/>
    <link href="data/styles.css" type="text/css" rel="stylesheet"/>
    <link href="files/workorderreport/styles.css" type="text/css" rel="stylesheet"/>
    <script src="resources/scripts/jquery-3.2.1.min.js"></script>
    <script src="resources/scripts/axure/axQuery.js"></script>
    <script src="resources/scripts/axure/globals.js"></script>
    <script src="resources/scripts/axutils.js"></script>
    <script src="resources/scripts/axure/annotation.js"></script>
    <script src="resources/scripts/axure/axQuery.std.js"></script>
    <script src="resources/scripts/axure/doc.js"></script>
    <script src="resources/scripts/messagecenter.js"></script>
    <script src="resources/scripts/axure/events.js"></script>
    <script src="resources/scripts/axure/recording.js"></script>
    <script src="resources/scripts/axure/action.js"></script>
    <script src="resources/scripts/axure/expr.js"></script>
    <script src="resources/scripts/axure/geometry.js"></script>
    <script src="resources/scripts/axure/flyout.js"></script>
    <script src="resources/scripts/axure/model.js"></script>
    <script src="resources/scripts/axure/repeater.js"></script>
    <script src="resources/scripts/axure/sto.js"></script>
    <script src="resources/scripts/axure/utils.temp.js"></script>
    <script src="resources/scripts/axure/variables.js"></script>
    <script src="resources/scripts/axure/drag.js"></script>
    <script src="resources/scripts/axure/move.js"></script>
    <script src="resources/scripts/axure/visibility.js"></script>
    <script src="resources/scripts/axure/style.js"></script>
    <script src="resources/scripts/axure/adaptive.js"></script>
    <script src="resources/scripts/axure/tree.js"></script>
    <script src="resources/scripts/axure/init.temp.js"></script>
    <script src="resources/scripts/axure/legacy.js"></script>
    <script src="resources/scripts/axure/viewer.js"></script>
    <script src="resources/scripts/axure/math.js"></script>
    <script src="resources/scripts/axure/jquery.nicescroll.min.js"></script>
    <script src="data/document.js"></script>
    <script src="files/workorderreport/data.js"></script>
    <script type="text/javascript" src="data/mask.js"></script>
    
    <script type="text/javascript">
      $axure.utils.getTransparentGifPath = function() { return 'resources/images/transparent.gif';};
      $axure.utils.getOtherPath = function() { return 'resources/Other.html';};
      $axure.utils.getReloadPath = function() { return 'resources/reload.html';};
    </script>
  </head>
  <body>
    <div id="base" class="">
      <!-- Unnamed (矩形) -->
      <div id="u38" class="ax_default _三级标题">
        <div id="u38_div" class=""></div>
        <div id="u38_text" class="text ">
          <p><span style="text-decoration:none;">盘点批次报表查询</span></p>
        </div>
      </div>
   <form id = "assetPdBatList" action="/eam/assetPdBatList" enctype="multipart/form-data" method="post">
      

      <!-- Unnamed (文本框) -->
      <div id="u43" class="ax_default text_field">
        <div id="u43_div" class=""></div>
        <input id="u43_input" type="text" name="pdBatId"  value="" class="u43_input"/>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u44" class="ax_default label">
        <div id="u44_div" class=""></div>
        <div id="u44_text" class="text ">
          <p><span style="text-decoration:none;">批次号</span></p>
        </div>
      </div>

      <!-- Unnamed (表格) -->
       <div id="u51" class="ax_default">
	<table id="table-a">
<tr><th>盘点批次</th><th>盘点编码</th><th>盘点开始时间</th><th>盘点结束时间</th><th>盘点公司</th>
	</tr>
 	<c:forEach items="${paging.dataList}" var="upl">      
      <tr>
   <td>${upl.pdBatId}</td>
   <td>${upl.pdBatCode}</td>
  <td>${upl.pdStartDate}</td>
  <td>${upl.pdEndDate}</td>
  <td>${upl.orgList}</td>
  <td><a href='assetPdList?batId=${upl.pdBatId}'>查看盘点列表</a></td>
      </tr>
     </c:forEach> 
       
     </table>
	 <input type="text" id="paging" value ="" hidden>   
	 <input type="text" id="turnPage" value ="" hidden>   
	 <div style="text-align:center; margin-top:10px;color: black;" id="venderfen">
	<p><div id="currentPageDiv"></div><a onclick="goPage(0)">  首页</a> <a onclick="goPage(-1)">上一页</a><a onclick="goPage(+1)">下一页</a></p>
</div> 
	
      </div>
     
      

      <!-- Unnamed (矩形) -->
      <div id="u98" class="ax_default label">
        <div id="u98_div" class=""></div>
        <div id="u98_text" class="text ">
          <p><span style="text-decoration:none;">查询条件</span></p>
        </div>
      </div>
<!-- search (矩形) -->
      <div id="u103" class="ax_default button" data-label="addBat">
        <div id="u103_div" class=""></div>
        <div id="u103_text" class="text ">
          <p><span style="text-decoration:none;"><a href="toAddPdBatPage">增加批次</a></span></p>
        </div>
      </div>
      <!-- search (矩形) -->
      <div id="u99" class="ax_default button" data-label="search">
        <div id="u99_div" class=""></div>
        <div id="u99_text" class="text ">
          <p><span style="text-decoration:none;"><a href="javascript:;" onclick="fsubmit()">搜索</a></span></p>
        </div>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u100" class="ax_default label">
        <div id="u100_div" class=""></div>
        <div id="u100_text" class="text ">
          <p><span style="text-decoration:none;">查询结果</span></p>
        </div>
      </div>

      <!-- download (矩形) -->
      <div id="u101" class="ax_default button" data-label="download">
        <div id="u101_div" class=""></div>
        <div id="u101_text" class="text ">
          <p><span style="text-decoration:none;"><a href="javascript:;" onclick="download1()">下载</a></span></p>
        </div>
      </div>
      <div id="massage_box" οnclick="hiddenMessage();">
			<div class="massage">
				<div class="header" οnmοusedοwn="MDown(massage_box);">
					<div style="display: inline; width: 150px; position: absolute">
						正在加载中 ... ...
					</div>
					<span
						onClick="massage_box.style.visibility='hidden'; mask.style.visibility='hidden'"
						style="float:right; display: inline; cursor: hand"><img src="images/workorderreport/mask1.jpg" height="30px" width="30px"/></span>
				</div>
				<div
					style="margin-top:20px; margin-left:20px; width:20px; height:20px; float:left;">
					<img src="images/workorderreport/mask.jpg" />
				</div>
				<div
					style="margin-top: 50px; width:136px; height: 128px; float: right;">
					查询请求已发送
					<br />
					等待返回查询结果
				</div>
			</div>
		</div>
		<div id="mask" οnclick="hiddenMessage();">
		</div>

</form>
      <!-- Unnamed (矩形) -->
      <div id="u102" class="ax_default link_button">
        <div id="u102_div" class=""></div>
        <div id="u102_text" class="text ">
          <p><span style="text-decoration:none;">&lt;-返回</span></p>
        </div>
      </div>
    </div>
    <script src="resources/scripts/axure/ios.js"></script>
    <script type="text/javascript">
    //$(document).ready(function(){
    	//XXX();
    //	var a = document.getElementById("assetPdBatList");
    	// a.submit();
    	
     //        });

 function fsubmit() {
	XXX();
	$.ajax({
        //几个参数需要注意一下
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "/eam/list" ,//url
            data: $('#workorderList').serialize(),
            success: function (result) {
                console.log(result);//打印服务端返回的数据(调试用)
                if (result.resultCode == 200) {
               	 hiddenMessage();
                }
                ;
            },
            error : function() {
           	 hiddenMessage();
                alert("查询异常，请重试！");
            }
        });
	 //var a = document.getElementById("workorderList");
      //      a.submit();
        }
        
 function addBat() {
		url=toAddPdBatPage;
	    window.location.href = url;
 }     
function download1() {
	 if (confirm("你确定下载吗？")) { 
		 var a = document.getElementById("basePath");
		 alert(a.value);
		 jQuery.download(a.value+"download","post","test.xls");
     	//window.open(a.value+"download");
     }  
        }
        
function goPage(page){
	var a = document.getElementById("basePath");
	 jQuery.pageTurn(a.value+"turnPage","post",page);
}

jQuery.download = function(url, method, filedir, filename){
    jQuery('<form action="'+url+'" method="'+(method||'post')+'">' +  // action请求路径及推送方法
                '<input type="text" name="filedir" value="'+filedir+'"/>' + // 文件路径
                '<input type="text" name="filename" value="'+filename+'"/>' + // 文件名称
            '</form>')
    .appendTo('body').submit().remove();
};      
        

jQuery.pageTurn = function(url, method, page){
 var inputValue = document.getElementById("paging").value;
 $("#page").val=page;
 XXX();
 $.ajax({
     //几个参数需要注意一下
         type: method,//方法类型
         url: url ,//url
         data: $('#workorderList').serialize(),
         dataType:"json",
         scriptCharset: "utf-8",
         success: function (result) {
        	hiddenMessage();
            // console.log();//打印服务端返回的数据(调试用)
        	 var html= $('#table-a'); //清空resText里面的所有内容
        	 html='<tr><th>维护编码</th><th>维护内容</th><th>设备编码</th><th>设备名称</th><th>设备中心</th><th>检修时间</th><th>维修人员</th><th>故障原因</th><th>错误编码</th>'+
        			'<th>触发时间</th><th>关闭时间</th><th>处理时间</th><th>停机时间</th><th>故障类型</th><th>故障废品</th><th>解决方案</th><th>超时分析</th><th>维修部位</th></tr>';
       	 	var dataList = result['dataList'];
       	  $("#paging").val(result);
        	 $.each(dataList, function(commentIndex, comment){
        		 html+=createHtml(comment);
        		 });
        	
        	 $('#table-a').html(html);
        	 $('#currentPageDiv').html("当前第 "+result['currentPage']+"  页/共   "+result['totalPage']+"  页");
         },
         error : function() {
        	 hiddenMessage();
             alert("查询异常，请重试！");
         }
     });
};  
</script>
  </body>
</html>
