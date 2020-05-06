<!DOCTYPE html PUBLIC "-//W3C//DtdHTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java"  contentType="text/html;charset=utf-8" 
    pageEncoding="utf-8"%>
     <%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%> 
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>


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
          <p><span style="text-decoration:none;">维护工单报表查询</span></p>
        </div>
      </div>
   <form id = "workorderList" action="/eam/list" enctype="multipart/form-data" method="post">
      <!-- Unnamed (下拉列表框) -->
      <div id="u39" class="ax_default droplist">
        <div id="u39_div" class=""></div>
        <select id="u39_input"  class="u39_input">
          <option class="u39_input_option" value="">不限</option>
          <option class="u39_input_option" value="维护性工单">维护性工单</option>
          <option class="u39_input_option" value="更正性工单">更正性工单</option>
        </select>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u40" class="ax_default label">
        <div id="u40_div" class=""></div>
        <div id="u40_text" class="text ">
          <p><span style="text-decoration:none;">工单类型</span></p>
        </div>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u41" class="ax_default label">
        <div id="u41_div" class=""></div>
        <div id="u41_text" class="text ">
          <p><span style="text-decoration:none;">工作中心</span></p>
        </div>
      </div>

      <!-- Unnamed (下拉列表框) -->
      <div id="u42" class="ax_default droplist">
        <div id="u42_div" class=""></div>
        <select id="u42_input" name="centerName" class="u42_input">
          <option class="u42_input_option" value="">不限</option>
          <option class="u42_input_option" value="夹层设备科">夹层设备科</option>
          <option class="u42_input_option" value="钢化一厂设备科">钢化设备科</option>
        </select>
      </div>

      <!-- Unnamed (文本框) -->
      <div id="u43" class="ax_default text_field">
        <div id="u43_div" class=""></div>
        <input id="u43_input" type="text" name="assetNum"  value="" class="u43_input"/>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u44" class="ax_default label">
        <div id="u44_div" class=""></div>
        <div id="u44_text" class="text ">
          <p><span style="text-decoration:none;">设备编码</span></p>
        </div>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u45" class="ax_default label">
        <div id="u45_div" class=""></div>
        <div id="u45_text" class="text ">
          <p><span style="text-decoration:none;">设备描述</span></p>
        </div>
      </div>

      <!-- Unnamed (文本框) -->
      <div id="u46" class="ax_default text_field">
        <div id="u46_div" class=""></div>
        <input id="u46_input" name="assetName" type="text" value="" class="u46_input"/>
      </div>

      <!-- Unnamed (文本框) -->
      <div id="u47" class="ax_default text_field">
        <!--<div id="u47_div" class=""></div>  -->
         <input type="date" id="startDate" name="startDate" value="${startDate}"  class="u49_input"/>
        
       </div>
      <!-- Unnamed (矩形) -->
      <div id="u48" class="ax_default label">
        <div id="u48_div" class=""></div>
        <div id="u48_text" class="text ">
          <p><span style="text-decoration:none;color:#D9001B;">*</span><span style="text-decoration:none;">起始日期</span></p>
        </div>
      </div>

      <!-- Unnamed (文本框) -->
      <div id="u49" class="ax_default text_field">
       <!-- <div id="u49_div" class=""></div> --> 
        <input id="endDate"   type="date" name="endDate" value="${endDate}" class="u49_input"/>
      </div>
 <input id="basePath"  type="hidden" value="<%=basePath%>" class="u49_input"/>
      <!-- Unnamed (矩形) -->
      <div id="u50" class="ax_default label">
        <div id="u50_div" class=""></div>
        <div id="u50_text" class="text ">
          <p><span style="text-decoration:none;color:#D9001B;">*</span><span style="text-decoration:none;">截止日期</span></p>
        </div>
      </div>

      <!-- Unnamed (表格) -->
       <div id="u51" class="ax_default">
	<table id="table-a">
<!-- '<tr><th>维护编码</th><th>维护内容</th><th>设备编码</th><th>设备名称</th><th>设备中心</th><th>检修时间</th><th>维修人员</th><th>故障原因</th><th>错误编码</th>
		<th>触发时间</th><th>关闭时间</th><th>处理时间</th><th>停机时间</th><th>故障类型</th><th>故障废品</th><th>解决方案</th><th>超时分析</th><th>维修部位</th>
	</tr>'
 	<c:forEach items="${paging.dataList}" var="upl">      
      <tr>
   <td>${upl.workOrderId}</td>
   <td>${upl.workOrderDescription}</td>
  <td>${upl.assetNumber}</td>
  <td>${upl.assetName}</td>
  <td>${upl.workcenter}</td>
  <td>${upl.plannedStartDate}</td>
  <td>${upl.repairMan}</td>
  <td>${upl.reason}</td>
  <td>${upl.faultcode}</td>
  <td>${upl.TTIME}</td>
  <td>${upl.CTIME}</td>
  <td>${upl.RTIME}</td>
  <td>${upl.MANAGEVALUE}</td>
  <td>${upl.faulttype}</td>
  <td>${upl.faultScrapt}</td>
  <td>${upl.solution}</td>
  <td>${upl.timeoutAnalysis}</td>
  <td>${upl.equipmentPart}</td> 
      </tr>
     </c:forEach> -->	
       
     </table>
	 <input type="text" id="paging" value ="" hidden>   
	 <input type="text" id="turnPage" value ="" hidden>   
	 <div style="text-align:center; margin-top:10px;color: black;" id="venderfen">
	<p><div id="currentPageDiv"></div><a onclick="goPage(0)">  首页</a> <a onclick="goPage(-1)">上一页</a><a onclick="goPage(+1)">下一页</a></p>
</div> 
	
      </div>
   <!-- Unnamed (单元格) -->
       <!-- Unnamed (矩形) -->
      <div id="u96" class="ax_default label">
        <div id="u96_div" class=""></div>
        <div id="u96_text" class="text ">
          <p><span style="text-decoration:none;">工单状态</span></p>
        </div>
      </div>  <!-- Unnamed (矩形) -->
      <div id="u96" class="ax_default label">
        <div id="u96_div" class=""></div>
        <div id="u96_text" class="text ">
          <p><span style="text-decoration:none;">工单状态</span></p>
        </div>
      </div>
      <!-- Unnamed (下拉列表框) -->
      <div id="u97" class="ax_default droplist">
        <div id="u97_div" class=""></div>
        <select id="u97_input" name="workOrderStatus" class="u97_input">
          <option class="u97_input_option" value="">不限</option>
          <option class="u97_input_option" value="关闭">关闭</option>
          <option class="u97_input_option" value="取消">取消</option>
          <option class="u97_input_option" value="发放">发放</option>
          <option class="u97_input_option" value="未发放">未发放</option>
        </select>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u98" class="ax_default label">
        <div id="u98_div" class=""></div>
        <div id="u98_text" class="text ">
          <p><span style="text-decoration:none;">查询条件</span></p>
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
$(document).ready(function(){
	XXX();
	 $.ajax({
         //几个参数需要注意一下
             type: "POST",//方法类型
             url: "/eam/list" ,//url
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
	// var a = document.getElementById("workorderList");
	 //a.submit();
	
	
     
})

function createHtml(comment){
	var html;
	 html += '<tr>';
	 html +='<th>'+comment['WorkOrderId']+'</th>';
	  html +='<th>'+comment['WorkOrderDescription']+'</th>';
	 html +='<th>'+comment['AssetNumber']+'</th>';
	 html +='<th>'+comment['assetName']+'</th>';
	 html +='<th>'+comment['workcenter']+'</th>';
	 html +='<th>'+comment['PlannedStartDate']+'</th>';
	 html +='<th>'+comment['repairMan']+'</th>';
	 html +='<th>'+comment['reason']+'</th>';
	 html +='<th>'+comment['faultcode']+'</th>';
	 html +='<th>'+comment['TTIME']+'</th>';
	 html +='<th>'+comment['CTIME']+'</th>';
	 html +='<th>'+comment['RTIME']+'</th>';
	 html +='<th>'+comment['MANAGEVALUE']+'</th>';
	 html +='<th>'+comment['faulttype']+'</th>';
	 html +='<th>'+comment['faultScrapt']+'</th>';
	 html +='<th>'+comment['solution']+'</th>';
	 html +='<th>'+comment['timeoutAnalysis']+'</th>';
	 html +='<th>'+comment['equipmentPart']+'</th>'; 

 html +='</tr>';
 return html;
}
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
