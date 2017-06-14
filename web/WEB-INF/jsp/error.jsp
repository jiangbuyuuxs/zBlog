<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/5
  Time: 13:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>错误</title>
  <%@include file="comm/jscss.jsp" %>
  <script>
    $(function(){
      $('.back').on('click',function(){
        window.history.go(-1);
      })
    });
  </script>
</head>
<body>
<div class="container">
  <div>
    <a class="btn btn-default" href="/">首页</a>
    <a class="btn btn-default back" href="#">返回</a>
  </div>
  <div class="has-error">
    <table class="table table-bordered">
      <tr>
        <td>目标</td>
        <td>消息</td>
      </tr>
      <tr class="danger">
        <td>${url}</td>
        <td>${message}</td>
      </tr>
    </table>
  </div>
</div>
</body>
</html>
