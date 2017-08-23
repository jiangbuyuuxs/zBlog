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
        .item {
            display: inline-block;
            margin: 10px 18px;
            border: 1px solid #F0F0F0;
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
            font-size: 12px;
            background: rgba(233, 238, 146, 0.79);
        }

        .item-price {
            width: 70px;
            display: inline-block;
            text-align: right;
            margin: 5px 0;
            color: #FF0003;
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
            font-size: 12px;
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
            cursor: hand;
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

        .item-list-panel {
            background: #cecece;
            width: 1170px;
            padding: 19px;
        }

        .sub-item-class-item {
            float: left;
            list-style: none;
            display: inline-block;
            margin: 4px;
        }

        .tmall {
            background: url("/resources/img/buy.png") -91px -1px;
            height: 29px;
            width: 85px;
            display: inline-block;
        }

        .taobao {
            background: url("/resources/img/buy.png") -1px -1px;
            height: 29px;
            width: 85px;
            display: inline-block;
        }
        .pagination a{
            cursor: hand;
        }
        .go-page{
            margin:20px 0;
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
            list-style: none;
        }
    </style>
    <script type="text/x-template" id="app">
        <div class="app">
            <div class="row item-class-nav">
                <ul class="item-class-container">
                    <li class="top-item"><a href="/">首页</a></li>
                    <li class="top-item"><a href="/buy/1">全部</a></li>
                    <li v-for="(itemClass,index) of itemClassList" class="top-item"><a class="item-class-a"
                                                                                       @click.prevent="showSub(itemClass.id,index)">{{itemClass.title}}</a>
                    </li>
                </ul>
            </div>
            <div class="row sub-item-class">
                <ul v-for="(subItemClass,index) of subItemClassList" class="sub-item-class-container"
                    :class="'sub-'+index">
                    <li v-for="itemClass of subItemClass" class="sub-item-class-item" @click.prevent="selectItemClassType(itemClass.hashCode)"><a
                            href="#">{{itemClass.title}}</a>
                    </li>
                </ul>
            </div>
            <div class="row item-list-panel">
                <div v-for="item of itemList" class="item">
                    <div>
                        <a :href="item.tbkUrl" target="_blank"><img class="item-image lazy"
                                                                    :data-original="item.imageUrl+'_240x240'"
                                                                    src="/resources/img/nopic.jpg"></a>
                    </div>
                    <div class="item-title">{{item.title}}</div>
                    <div class="item-info">
                        <span class="item-price h3"><span class="h4">￥</span>{{item.price}}</span>
                        <span class="item-favourable">{{item.favourable.title}}</span>
                        <span class="item-buy"><a :href="item.tbkUrl" target="_blank"><span
                                :class="item.shopType"> </span></a></span>
                    </div>
                    <div><span class="item-sales-volume">当前销量:{{item.salesVolume}}</span></div>
                </div>
            </div>
            <div class="page-bar row" :style="{visibility:pageNum>1?'visible':'hidden'}">
                <div class="col-lg-2">
                    <select class="form-control go-page" @change.prevent="selectPage">
                        <option v-for="page in pageNum" :value="page">{{page}}</option>
                    </select>
                </div>
                <div class="col-lg-7">
                    <ul class="pagination">
                        <li><a href="#" @click="prePage"><span>&laquo;</span></a></li>
                        <li v-for="i in pageNums" :class="{active:i===curPage}">
                            <%--<a v-if="i!=='...'" :href="'/buy/'+i">{{i}}</a>--%>
                            <a v-if="i!=='...'" @click.prevent="changePage(i)">{{i}}</a>
                            <a v-if="i==='...'" @click.prevent href="#">...</a>
                        </li>
                        <li><a href="#" @click="nextPage"><span>&raquo;</span></a></li>
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
                        curPage: 1,
                        pageNums: [],
                        itemClass:'',
                        pageNum:${pageNum},
                        itemClassList:${itemClassList},
                        subItemClassList: [],
                        loadData: []
                    }
                },
                methods: {
                    showSub: function (id, index) {
                        if (!this.loadData[index]) {
                            var url = '/buy/subitemclass'
                            this.$http.get(url, {params: {id: id}}).then(function (response) {
                                Vue.set(this.subItemClassList, index, response.data.data.subItemClassList);
                                this.loadData[index] = true;
                            });
                        }
                        this.$nextTick(function () {
                            $('.sub-item-class-container').hide();
                            $('.sub-' + index).show();
                        })
                    },
                    selectItemClassType: function (itemClass) {
                        this.itemClass = itemClass;
                        this.fetchData(1);
                    },
                    prePage: function () {
                        if(this.curPage>2)
                            this.fetchData(this.curPage-1);
                    },
                    nextPage: function () {
                        if(this.curPage<this.pageNum-1)
                            this.fetchData(this.curPage+1);
                    },
                    changePage: function (page) {
                        this.fetchData(page);
                    },
                    selectPage: function (e) {
                        this.fetchData(e.currentTarget.value-0);
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
                    },
                    fetchData: function (page) {
                        var url = '/buy/itemlist';
                        this.$http.get(url, {params: {page: page,itemClass:this.itemClass}}).then(function (response) {
                            this.itemList = response.data.data.itemList;
                            this.curPage = page;
                            this.pageNum = response.data.data.pageNum;
                            this.$nextTick(function () {
                                $('img.lazy').lazyload();
                            });
                            this.getPageNum(this.startPage, this.endPage);
                        });
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
                    }
                },
                created: function () {
                    this.subItemClassList.length = this.itemClassList.length;
                    this.loadData.length = this.itemClassList.length;

                    this.getPageNum(this.startPage, this.endPage);
                    this.$nextTick(function () {
                        $('img.lazy').lazyload();
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
        <a class="go-top-btn"></a>
    </li>
</ul>
<%@include file="../comm/footer.jsp" %>
</body>
</html>
