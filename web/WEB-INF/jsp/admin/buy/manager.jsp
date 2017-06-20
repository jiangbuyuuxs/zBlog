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
    <%@include file="../../comm/jscss.jsp" %>
    <style>
        .no-relation-class-list, .sub-item-class-list, .top-item-class-list {
            height: 600px;
            overflow-y: auto;
            border: 1px solid #cecece;
        }

        .no-relation-class-list li, .sub-item-class-list li, .top-item-class-list li {
            border-bottom: 1px solid #cecece;
            list-style: none;
            line-height: 30px;
            cursor: hand;
        }

        .no-relation-class-list li:hover, .sub-item-class-list li:hover, .top-item-class-list li:hover {
            background-color: #dddddd;
        }

        .selected-active {
            background-color: #cecece;
        }
    </style>
    <script>
        $(function () {
            var noRelation = $('.no-relation-class-list');
            var subItemClass = $('.sub-item-class-list');
            noRelation.on('click', 'li', addToRootItemClass);
            subItemClass.on('click', 'li', addToRootItemClass);

            function addToRootItemClass() {
                var self = $(this).find('.item-class-item');
                var id = self.attr('data-id');
                var selectedTopId = $('.selected-top-id').val();
                var method = self.attr('data-method');
                if (id == '') {
                    alert('未选定要添加的分类');
                    return false;
                }
                if (selectedTopId !== '') {
                    $.ajax('/admin/buy/update', {
                        data: {
                            id: id,
                            selectedTopId: selectedTopId,
                            method: method
                        },
                        dataType: 'json',
                        success: function (data) {
                            if (!data.success) {
                                alert(data.message);
                            } else {
                                var to = subItemClass;
                                if(method == 'remove'){
                                    method= 'set';
                                    to = noRelation;
                                }else if(method == 'set'){
                                    method= 'remove';
                                }
                                self.attr('data-method', method);
                                self.parent().off('click');
                                self.parent().appendTo(to);
                                self.parent().on('click', addToRootItemClass);
                            }
                        }
                    });
                }
            }


            var topItem = $('.top-item-class-list');
            topItem.on('click', 'li', selectTopItem);
            function selectTopItem() {
                var self = $(this);
                topItem.find('.selected-active').removeClass('selected-active');
                self.addClass('selected-active');
                var selectedItem = self.find('.item-class-item');
                var id = selectedItem.attr('data-id');
                $.ajax('/admin/buy/get', {
                    data: {
                        id: id
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            var selectedItemTitle = selectedItem.text();
                            $('.selected-top-title').text('').text(selectedItemTitle);
                            $('.selected-top-id').val('').val(id);
                            var subItemClassList = data.subItemClassList;
                            subItemClass.empty();
                            if(subItemClassList.length>0){
                                createItemList(subItemClassList);
                            }
                        }
                    }
                });
            }

            function createItemList(itemClassList){
                for(var i= 0,j=itemClassList.length;i<j;i++){
                    $('.sub-item-class-list').append($('<li><span class="item-class-item" data-method="remove" data-id="'+itemClassList[i].id+'">'+itemClassList[i].title+'</span></li>'));
                }
            }
        });
    </script>
</head>
<body class="container">
<a href="/admin/buy/delete">删除数据</a>

<div class="row">
    <div class="col-lg-4">
        <p>顶级分类</p>
        <ul class="top-item-class-list">
            <c:forEach var="topItemClass" items="${topItemClassList}">
                <li><span class="item-class-item" data-id="${topItemClass.id}">${topItemClass.title}</span></li>
            </c:forEach>
        </ul>
        <input type="hidden" class="selected-top-id" name="selectTop" value="${defaultSelectedItem.id}">
    </div>
    <div class="col-lg-4">
        <p>[<span class="selected-top-title">${defaultSelectedItem.title}</span>]的儿子分类</p>
        <ul class="sub-item-class-list">
            <c:forEach var="subItemClass" items="${subItemClassList}">
                <li><span class="item-class-item" data-method="remove"
                          data-id="${noRelationClass.id}">${subItemClass.title}</span></li>
            </c:forEach>
        </ul>
    </div>
    <div class="col-lg-4">
        <p>未设定的分类</p>
        <ul class="no-relation-class-list">
            <c:forEach var="noRelationClass" items="${noRelationClassList}">
                <li><span class="item-class-item" data-method="set"
                          data-id="${noRelationClass.id}">${noRelationClass.title}</span></li>
            </c:forEach>
        </ul>
    </div>
</div>
<%@include file="../../comm/footer.jsp" %>
</body>
</html>
