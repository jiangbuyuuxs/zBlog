<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/6/19
  Time: 11:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>买买买</title>
  <%@include file="../comm/jscss.jsp" %>
  <script type="text/javascript" src="/resources/jQuery/plug/lazyload/jquery.lazyload.js"></script>
  <style>
    .item{
      display: inline-block;
      margin:0 20px;
    }
    .item-title{
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      width:200px;
    }
    .item-image{
      width:200px;
      height:200px;
    }
  </style>
  <script>
    $(function() {
      $("img.lazy").lazyload();
    });
  </script>
</head>
<body class="container">
  <div>
    <c:forEach var="item" items="${itemList}" >
    <div class="item">
      <div><img class="item-image lazy" data-original="${item.imageUrl}_200x200" src="/resources/img/nopic.jpg" ></div>
      <div class="item-title">${item.title}</div>
      <div>
        <span>${item.salesVolume}</span>
        <span></span>
        <span>${item.price}</span>
      </div>
    </div>
    </c:forEach>
  </div>
  <%@include file="../comm/footer.jsp" %>
</body>
</html>
