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
    <title>PDList</title>
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
    
  </head>
  <body>
    <div id="base" class="">
      <!-- Unnamed (矩形) -->
      <div id="u38" class="ax_default _三级标题">
        <div id="u38_div" class=""></div>
        <div id="u38_text" class="text ">
          <p><span style="text-decoration:none;">盘点表报表查询</span></p>
        </div>
      </div>
   <form id = "assetPdList" action="/eam/assetPdList" enctype="multipart/form-data" method="post">
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
<tr><th>运营组织</th><th>部门</th><th>使用人工号</th><th>使用人名字</th><th>计算机名</th><th>资产编号</th><th>型号</th><th>基本配置</th>
		<th>序列号</th><th>盘点时间</th><th>盘点批次ID</th><th>结果</th>
	</tr>'
 	<c:forEach items="${paging.dataList}" var="upl">      
      <tr>
   <td>${upl.organizationName}</td>
   <td>${upl.department}</td>
  <td>${upl.jobNum}</td>
  <td>${upl.userName}</td>
  <td>${upl.description}</td>
  <td>${upl.assetNumber}</td>
  <td>${upl.assetModel}</td>
  <td>${upl.allocation}</td>
  <td>${upl.serialNumber}</td>
  <td>${upl.pdTime}</td>
  <td>${upl.pdBatId}</td>
  <td>${upl.status}</td>
      </tr>
     </c:forEach> 
       
     </table>
	 <input type="text" id="paging" value ="" hidden>   
	 <input type="text" id="turnPage" value ="" hidden>   
	 <div style="text-align:center; margin-top:10px;color: black;" id="venderfen">
	<p><div id="currentPageDiv"></div><a onclick="goPage(0)">  首页</a> <a onclick="goPage(-1)">上一页</a><a onclick="goPage(+1)">下一页</a></p>
</div> 
	
      </div>
   <!-- Unnamed (单元格) -->
       <!-- Unnamed (矩形) -->
      <div id="u41" class="ax_default label">
        <div id="u41_div" class=""></div>
        <div id="u41_text" class="text ">
          <p><span style="text-decoration:none;">员工号</span></p>
        </div>
         <!-- Unnamed (文本框) -->
      <div id="u42" class="ax_default text_field">
        <div id="u42_div" class=""></div>
        <input id="u42_input" type="text" name="jobNum"  value="" class="u42_input"/>
      </div>
      </div>  <!-- Unnamed (矩形) -->
      <div id="u96" class="ax_default label">
        <div id="u96_div" class=""></div>
        <div id="u96_text" class="text ">
          <p><span style="text-decoration:none;">批次号</span></p>
        </div>
      </div>
       <!-- Unnamed (文本框) -->
      <div id="u43" class="ax_default text_field">
        <div id="u43_div" class=""></div>
        <input id="u43_input" type="text" name="assetNum"  value="" class="u43_input"/>
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
function goPage(page){
	var a = document.getElementById("basePath");
	 jQuery.pageTurn(a.value+"turnPage","post",page);
}

jQuery.pageTurn = function(url, method, page){
 var inputValue = document.getElementById("paging").value;
 $("#page").val=page;
};  
</script>
  </body>
</html>
