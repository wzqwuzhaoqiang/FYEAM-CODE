﻿<%@ page language="java"  contentType="text/html;charset=UTF-8" 
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
    <title>login</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <link href="resources/css/axure_rp_page.css" type="text/css" rel="stylesheet"/>
    <link href="data/styles.css" type="text/css" rel="stylesheet"/>
    <link href="files/login/styles.css" type="text/css" rel="stylesheet"/>
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
    <script src="files/login/data.js"></script>
    <script type="text/javascript">
      $axure.utils.getTransparentGifPath = function() { return 'resources/images/transparent.gif'; };
      $axure.utils.getOtherPath = function() { return 'resources/Other.html'; };
      $axure.utils.getReloadPath = function() { return 'resources/reload.html'; };
    </script>
  </head>
  <body>
    <div id="base" class="">

      <!-- Unnamed (矩形) -->
      <div id="u29" class="ax_default box_2">
        <div id="u29_div" class=""></div>
        <div id="u29_text" class="text " style="display:none; visibility: hidden">
          <p></p>
        </div>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u30" class="ax_default box_2">
        <div id="u30_div" class=""></div>
        <div id="u30_text" class="text ">
          <p><span style="text-decoration:none;">信息提示</span></p>
        </div>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u31" class="ax_default box_1">
        <div id="u31_div" class=""></div>
        <div id="u31_text" class="text ">
          <p><span style="text-decoration:none;">输入用户名</span></p>
        </div>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u32" class="ax_default box_1">
        <div id="u32_div" class=""></div>
        <div id="u32_text" class="text ">
          <p><span style="text-decoration:none;">输入密码</span></p>
        </div>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u33" class="ax_default box_1">
        <div id="u33_div" class=""></div>
        <div id="u33_text" class="text ">
          <p><span style="text-decoration:none;">登录界面</span></p>
        </div>
      </div>

      <!-- loginButton (矩形) -->
      <div id="u34" class="ax_default box_1" data-label="loginButton" title="输入">
        <div id="u34_div" class=""></div>
        <div id="u34_text" class="text ">
          <p><span style="text-decoration:none;"><a href="loading">登录</a></span></p>
        </div>
      </div>

      <!-- usernameInput1 (文本框) -->
      <div id="u35" class="ax_default text_field" data-label="usernameInput1" title="用户名">
        <div id="u35_div" class=""></div>
        <input id="u35_input" type="text" value="" class="u35_input" title="用户名"/>
      </div>

      <!-- passwordInput1 (文本框) -->
      <div id="u36" class="ax_default text_field" data-label="passwordInput1" title="密码">
        <div id="u36_div" class=""></div>
        <input id="u36_input" type="password" value="" class="u36_input" title="密码"/>
      </div>

      <!-- Unnamed (矩形) -->
      <div id="u37" class="ax_default">
        <div id="u37_div" class=""></div>
        <div id="u37_text" class="text ">
          <p><span style="text-decoration:underline ;">忘记密码</span></p>
        </div>
      </div>
    </div>
    <script src="resources/scripts/axure/ios.js"></script>
    <script type="text/javascript">
    function fsubmit() {
	alert("11111");
	window.location.href="loading";
	//window.open("http://127.0.0.1:9001/eam/loading","_blank");

    }
    </script>
  </body>
</html>
