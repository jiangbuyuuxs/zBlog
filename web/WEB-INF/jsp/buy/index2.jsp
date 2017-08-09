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
    <%@include file="../comm/vue.jsp" %>
    <script type="text/javascript" src="/resources/jQuery/plug/lazyload/jquery.lazyload.js"></script>
    <style>
        * {
            color: #666;
        }

        .item {
            display: inline-block;
            margin: 10px 15px;
            border: 1px solid #F0F0F0;
        }

        .item-title {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            width: 250px;
            font-size: 12px;
            line-height: 25px;
            padding: 0 2px;
            background: #a7cec9;
        }

        .item-image {
            width: 250px;
            height: 250px;
        }

        .item-info{
            line-height: 32px;
        }
        .item-buy{
            height: 29px;
            width: 85px;
            display: inline-block;
            margin:1px 0;
        }
        .tmall {
            background: url("/resources/img/buy.png") -91px -1px;
        }
        .taobao {
            background: url("/resources/img/buy.png") -1px -1px;
        }

        .item-favourable {
            width: 85px;
            text-align: center;
            display: inline-block;
            font-size: 12px;
            line-height: normal;
        }

        .item-price {
            width: 70px;
            display: inline-block;
            text-align: right;
            margin: 2px 0;
            color: #FF0003;
        }
        .item-sales-volume {
            width: 100%;
            display: inline-block;
            text-align: center;
            font-size: 12px;
            background: rgba(233, 238, 146, 0.79);
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
            display: inline-block;
            height: 35px;
        }

        .sub-item-class-container li {
            height: 28px;
        }

        .sub-item-class-container li a {
            color: #666;
        }

        .top-item a {
            height: 35px;
            line-height: 35px;
            position: relative;
            display: inline-block;
            width: 100%;
            padding: 0 40px;
            text-align: center;
            text-decoration: none;
            border-bottom: 2px solid transparent;
            cursor: hand;
            color: #666;
        }

        .sub-item-class-item a {
            border-bottom: 2px solid transparent;
            text-decoration: none;
        }

        .sub-item-class-item a:hover {
            color: #FF0003;
        }

        .top-item a:hover, .item-class-a.active {
            border-bottom: 2px solid #FF0003;
            color: #FF0003;
        }

        .item-class-nav {
            position: relative;
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

        .item-list-panel {
            background: #cecece;
            width: 1170px;
            padding: 10px 19px 19px 19px;
        }

        .sub-item-class {
            background: #ffffff;
            position: absolute;
            top: 35px;
            margin: 0 0 0 40px;
            width: 600px;
            left: 224px;
        }

        .sub-item-class-container {
            border: 1px solid #FF0003;
            border-top: none;
            width:600px;
        }

        .sub-item-class-item {
            list-style: none;
            display: inline-block;
            margin: 4px;
        }

        .pagination a {
            cursor: hand;
        }

        .go-page {
            margin: 20px 0;
        }

        ul.sub-item-class-container {
            padding: 0px;
        }

        ul.item-class-container {
            margin-bottom: 0;
            width:1170px;
        }

        .item-class-path {
            margin-bottom: 0px;
        }
        .item-order-container{
            margin:10px 0 10px 20px;
        }
        .go-top-btn{
            width:50px;
            height:50px;
            display: block;
            background: url("/resources/img/go-top.png") no-repeat;
        }
        .tool-bar{
            position: fixed;
            right:40px;
            bottom:40px;
        }
    </style>
    <script type="text/x-template" id="app">
        <div class="app">
            <div class="row item-class-nav">
                <ul class="item-class-container">
                    <li class="top-item"><a href="/">首页</a></li>
                    <li class="top-item"><a href="/buy/1">全部</a></li>
                    <div style="display: inline;" @mouseleave="hideSub">
                        <li v-for="itemClass in topItemClassList" class="top-item"><a class="item-class-a"
                                                                                      :data-id="itemClass.id"
                                                                                      @mouseover.prevent="showSub(itemClass.id)">{{itemClass.title}}</a>
                        </li>
                        <div class="row sub-item-class">
                            <ul v-for="subItemClass in subItemClassList" class="sub-item-class-container"
                                :class="'sub-'+subItemClass[0].parentId"
                                :data-parentid="subItemClass[0].parentId">
                                <li v-for="itemClass of subItemClass" class="sub-item-class-item"><a
                                        :href="'/buy/1?itemclass='+itemClass.hashCode"
                                        :data-item-hash="itemClass.hashCode">{{itemClass.title}}</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </ul>
            </div>
            <div class="row item-list-panel">
                <ol class="breadcrumb item-class-path" v-if="topItemClass!=null">
                    <li class="active">{{topItemClass.title}}</li>
                    <li class="active">{{subItemClass.title}}</li>
                </ol>
                <div class="row item-order-container">
                    <div class="btn-group" >
                        <a class="btn btn-xs"
                           :class="sortSale==1?'btn-danger':'btn-default'"
                           :href="'/buy/1'+qStr">销量</a>
                        <a class="btn btn-xs"
                           :class="sortPrice==-1?'btn-default':(this.sortPrice==0?'btn-success':'btn-danger')"
                           :href="'/buy/1'+priceQStr">价格</a>
                    </div>
                </div>
                <div v-for="item of itemList" class="item">
                    <div>
                        <a :href="item.tbkUrl" target="_blank"><img class="item-image lazy"
                                                                    :data-original="item.imageUrl+'_250x250'"
                                                                    src="/resources/img/nopic.jpg"></a>
                    </div>
                    <div class="item-title">{{item.title}}</div>
                    <div class="item-info">
                        <span class="item-price h3"><span class="h4">￥</span>{{item.price}}</span>
                        <span class="item-favourable">{{item.favourable.title}}</span>
                        <a class="item-buy" :class="item.shopType" :href="item.tbkUrl" target="_blank"></a>
                    </div>
                    <div><span class="item-sales-volume">当前销量:{{item.salesVolume}}</span></div>
                </div>
            </div>
            <div class="page-bar row" :style="{visibility:pageNum>1?'visible':'hidden'}">
                <div class="col-lg-2">
                    <select class="form-control go-page" @change.prevent="selectPage">
                        <option v-for="page in pageNum" :selected="page==curPage" :value="page">{{page}}</option>
                    </select>
                </div>
                <div class="col-lg-7">
                    <ul class="pagination">
                        <li v-if="curPage>1"><a href="#" :href="'/buy/'+(curPage==1?1:curPage-1)+qStr"><span>&laquo;</span></a></li>
                        <li v-for="i in pageNums" :class="{active:i===curPage}">
                            <a v-if="i!=='...'&&i!=curPage" :href="'/buy/'+i+qStr">{{i}}</a>
                            <a v-if="i==curPage" href="#" @click.prevent>{{i}}</a>
                            <a v-if="i==='...'">...</a>
                        </li>
                        <li v-if="curPage<pageNum"><a href="#"
                               :href="'/buy/'+(curPage==pageNum?pageNum:curPage+1)+qStr"><span>&raquo;</span></a></li>
                    </ul>
                </div>
            </div>
        </div>
    </script>
    <script>
        $(function () {

            //分页条中间(除去首页和末页)显示的页数
            var pageBarPageNum = 7;
            var halfPageBarPageNum = Math.floor(pageBarPageNum / 2);
            var app = {
                template: '#app',
                data: function () {
                    return {
                        itemList:${itemList},
                        curPage: ${curPage},
                        pageNums: [],//显示的页数
                        pageNum:${pageNum},//总页数
                        qStr: '${qStr}',
                        sortPrice:-1,
                        sortSale:1,//默认按销售量排序
                        topItemClass:${topItemClass},
                        subItemClass:${subItemClass},
                        topItemClassList:${topItemClassList},
                        subItemClassList:${subItemClassList}
                    }
                },
                methods: {
                    hideSub: function () {
                        $('.sub-item-class-container').hide();
                        $('.item-class-a').removeClass('active');
                    },
                    showSub: function (parentId) {
                        $('.sub-item-class-container').hide();
                        $('.sub-' + parentId).show();
                        $('.item-class-a').removeClass('active');
                        $('.item-class-a[data-id=' + parentId + ']').addClass('active');
                    },
                    selectPage: function (e) {
                        var page = e.currentTarget.value - 0;
                        if (page != this.curPage)
                            window.location.href = '/buy/' + page + this.qStr;
                    },
                    getPageNum: function (startPage, endPage) {
                        this.pageNums = [];
                        for (var i = 0, j = startPage, k = endPage; j <= k; j++) {
                            this.pageNums[i++] = j;
                        }
                        if (this.startPage > 2)
                            this.pageNums.unshift("...");
                        if (this.startPage > 1) {
                            this.pageNums.unshift(1);
                        }
                        if (this.endPage < this.pageNum) {
                            this.pageNums.push("...", this.pageNum);
                        }
                    }
                },
                computed: {
                    startPage: function () {
                        //页码总数不足或者页码是最初几个
                        if (this.pageNum <= pageBarPageNum || this.curPage <= 1 + (1 + halfPageBarPageNum)) {
                            return 1;
                        } else {
                            //当当前页码之后的页码数不足时
                            if (this.curPage + halfPageBarPageNum > this.pageNum - 1)
                                return this.pageNum - pageBarPageNum;
                            return this.curPage - halfPageBarPageNum;
                        }
                    },
                    endPage: function () {
                        //页码总数不足或者页码已经是最后几个
                        if (this.pageNum <= pageBarPageNum || this.curPage >= this.pageNum - (1 + halfPageBarPageNum)) {
                            return this.pageNum;
                        } else {
                            //当当前页码之前的页码不足时
                            if (this.curPage - halfPageBarPageNum < 2)
                                return pageBarPageNum + 1;
                            return this.curPage + halfPageBarPageNum;
                        }
                    },
                    priceQStr: function () {
                        var search = document.location.search;
                        console.log(search,hasSortPrice);
                        //不存在查询条件
                        if(search==''){
                            return this.qStr + '?sort=price_0';
                        }else{
                            var priceOrderFlag = 'sort=price_';
                            var hasSortPrice = search.indexOf(priceOrderFlag);
                            //存在价格排序这个条件
                            if(hasSortPrice>-1) {
                                var forward = search.substr(hasSortPrice + priceOrderFlag.length, 1);
                                this.sortPrice = Math.abs(forward - 1);
                                this.sortSale = 0;
                                //只有一个排序参数
                                if(search.indexOf('?'+priceOrderFlag)==0)
                                    return this.qStr + '?sort=price_'+this.sortPrice;
                                return this.qStr + '&sort=price_'+this.sortPrice;
                            }else{
                                return this.qStr + '&sort=price_0';
                            }
                        }
                    }
                },
                created: function () {
                    this.getPageNum(this.startPage, this.endPage);
                    this.$nextTick(function () {
                        $('img.lazy').lazyload();
                        $('.sub-item-class-container').hide();
                    });
                }
            };
            new Vue({
                el: '.buy-main-panel',
                components: {
                    'app': app
                }
            });
            $('#go-top').on('click', function () {
                $('body').animate({
                    scrollTop:0
                },{
                    duration:500,
                    easing:'linear'
                });
            });
            $(window).on('scroll', function () {
                if($('body').scrollTop() > 0){
                    $('.tool-bar').removeClass('hidden');
                }else{
                    $('.tool-bar').addClass('hidden');
                }
            });
        });
    </script>
</head>
<body class="container">
<div class="buy-main-panel">
    <div class="row head">
        <p class="head-img-container"></p>
    </div>
    <app></app>
</div>
<ul class="tool-bar hidden">
    <li id="go-top">
        <a class="go-top-btn" title="回到顶部"></a>
    </li>
</ul>
<%@include file="../comm/footer.jsp" %>
</body>
</html>
