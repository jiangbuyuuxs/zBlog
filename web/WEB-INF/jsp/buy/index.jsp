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
        .item {
            display: inline-block;
            margin: 10px 18px;
            border:1px solid #F0F0F0;
        }

        .item-title {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            width: 240px;
            font-size: 12px;
            line-height: 25px;
            padding: 0 2px;
            background: #a7cec9;
        }

        .item-image {
            width: 240px;
            height: 240px;
        }

        .item-sales-volume {
            width: 100%;
            display: inline-block;
            text-align: center;
            font-size:12px;
            background: rgba(233, 238, 146, 0.79);
        }

        .item-price {
            width: 70px;
            display: inline-block;
            text-align: right;
            margin: 5px 0 ;
            color:#FF0003;
        }

        .no-item {
            text-align: center;
            background: #cecece;
            height: 300px;
            width: 100%;
            background: url("/resources/img/silent.jpg") repeat;
        }

        .item-class-container li {
            list-style: none;
            float: left;
            height: 35px;
        }

        .item-favourable {
            width: 77px;
            text-align: center;
            display: inline-block;
            font-size:12px;
        }

        .top-item a {
            height: 35px;
            line-height: 35px;
            position: relative;
            display: inline-block;
            width: 100%;
            padding: 0 40px;
            text-align: center;
            background: #eeeeee;
            color: #000000;
            text-decoration: none;
            border-left: 2px solid #eeeeee;
            border-right: 2px solid #eeeeee;
        }

        .top-item a:hover {
            border-left: 2px solid #abd4f3;
            border-right: 2px solid #abd4f3;
            color: #abd4f3;
        }
        li a.active {
            border-left: 2px solid #abd4f3;
            border-right: 2px solid #abd4f3;
            color: #abd4f3;
        }

        .item-class-nav {
            background: #dddddd;
        }

        .head {
            background: #cecece;
        }

        .head-img-container {
            width: 960px;
            height: 200px;
            margin: 0 auto;
            background: url("/resources/img/silent.jpg");
        }

        .item-list-container {
            background: #cecece;
            width: 1170px;
            padding: 19px;
        }

        .sub-item-class-item{
            float:left;
            list-style: none;
            display: inline-block;
            margin: 4px;
        }
        .tmall{
            background: url("/resources/img/buy.png") -91px -1px;
            height:29px;
            width:85px;
            display: inline-block;
        }
        .taobao{
            background: url("/resources/img/buy.png") -1px -1px;
            height:29px;
            width:85px;
            display: inline-block;
        }
    </style>
    <script>
        $(function () {
            $('img.lazy').lazyload();

            $('.item-class-a').each(function (position,item) {
                var id = $(item).attr('data-id');
                var liObj = $(item).parent();
                $.ajax('/buy/getsubitemclass', {
                    data: {id:id},
                    dataType: 'json',
                    success: function (data) {
                        if(data.success){
                            var ul = $('<ul class="sub-item-class-container sub-item-class-container-'+id+'"></ul>');
                            var subItemClassList = data.subItemClassList;
                            for(var i= 0,j=subItemClassList.length;i<j;i++){
                                ul.append($('<li class="sub-item-class-item"><a data-id="'+subItemClassList[i].id+'" href="#">'+subItemClassList[i].title+'</a></li>'));
                            }
                            ul.hide();
                            $('.sub-item-class').append(ul);
                            liObj.on('click', function () {
                                $(this).parent().find('.active').removeClass('active');
                                $(this).find('a').addClass('active');
                                $('.sub-item-class-container').hide();
                                $('.sub-item-class-container-'+id).show();
                            });
                            $('.sub-item-class-item a').on('click', function () {

                            });
                        }
                    }
                });
            });

        });
    </script>
</head>
<body class="container">
<div>
    <div class="row head">
        <p class="head-img-container"></p>
    </div>
    <div class="row item-class-nav">
        <ul class="item-class-container">
            <li class="top-item"><a href="/">首页</a></li>
            <li class="top-item"><a href="/buy/1">全部</a></li>
            <c:forEach var="itemClass" items="${itemClassList}">
                <li class="top-item"><a class="item-class-a" href="#" data-id="${itemClass.id}">${itemClass.title}</a></li>
            </c:forEach>
        </ul>
    </div>
    <div class="row sub-item-class">
    </div>
    <div class="row item-list-container">
        <c:forEach var="item" items="${itemList}">
            <div class="item">
                <div>
                    <a href="${item.tbkUrl}" target="_blank"><img class="item-image lazy" data-original="${item.imageUrl}_240x240"
                          src="/resources/img/nopic.jpg"></a>
                </div>
                <div class="item-title">${item.title}</div>
                <div class="item-info">
                    <span class="item-price h3"><span class="h4">￥</span>${item.price}</span>
                    <span class="item-favourable">${item.favourable.title}</span>
                    <span class="item-buy"><a href="${item.tbkUrl}" target="_blank"><span class="${item.shopType}"> </span></a></span>
                </div>
                <div><span class="item-sales-volume">当前销量:${item.salesVolume}</span></div>
            </div>
        </c:forEach>
        <c:if test="${itemList.size()==0}">
            <div class="no-item">
                .
            </div>
        </c:if>
    </div>
</div>
<%@include file="../comm/footer.jsp" %>
</body>
</html>
