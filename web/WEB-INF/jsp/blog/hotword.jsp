<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>热词搜索页</title>
    <%@include file="../comm/jscss.jsp" %>
    <%@include file="../comm/vue.jsp" %>
    <link rel="stylesheet" type="text/css" href="/resources/css/index.css">
    <script type="text/x-template" id="blog-panel-template">
        <div class="col-lg-9 col-md-8 col-sm-12 col-xs-12 blog-panel">
            <div class="row blog-list-head">
                <div class="col-lg-10 col-md-9 col-sm-9 col-xs-8 title">标题</div>
                <div class="col-lg-2 col-md-3 col-sm-3 col-xs-4">发布时间</div>
            </div>
            <div class="blog-list">
                <div v-for="(blog,index) in blogList">
                    <div class="row">
                        <div class="col-lg-10 col-md-9 col-sm-9 col-xs-8 padding2px">[标题:]<a :title="blog.title"
                                                                                             :href="'/blog/blog/'+blog.id">{{blog.title}}</a>
                        </div>
                        <div class="col-lg-2 col-md-3 col-sm-3 col-xs-4 padding2px">{{blog.createDate}}</div>
                    </div>
                    <div class="row">
                        <div class="summary">[摘要:]{{blog.texts}}</div>
                    </div>
                </div>
            </div>
            <div class="page-bar" :style="{visibility:pageNum>1?'visible':'hidden'}">
                <ul class="pagination">
                    <li><a href="#" @click="prePage"><span>&laquo;</span></a></li>
                    <li v-for="i in pageNum" :class="{active:i===curPage}"><a href="#" @click="changePage(i)"
                                                                              :data-page="i">{{i}}</a></li>
                    <li><a href="#" @click="nextPage"><span>&raquo;</span></a></li>
                </ul>
            </div>
        </div>
    </script>
    <script>
        $(function () {

            var blogPanel = {
                template: '#blog-panel-template',
                data: function () {
                    return {
                        blogList: [],
                        pageNum: 0,
                        curPage: 1,
                        hashcode:'${hashcode}',
                        hotWord:'${hotWord}'
                    }
                },

                methods: {
                    prePage: function () {
                        this._changePage(this.curPage - 1);
                    },

                    nextPage: function () {
                        this._changePage(this.curPage + 1);
                    },

                    changePage: function (page) {
                        this._changePage(page);
                    },
                    _changePage: function (page) {
                        if (page < 1 || page > this.pageNum || page === this.curPage)
                            return
                        var url = '/blog/hotword/list/' + this.hashcode + '?page=' + page + '&pageSize=10';
                        this.$http.get(url).then(function (response) {
                            this.blogList = response.data.data.blogList;
                            this.curPage = page;
                        }, function (response) {
                            console.log(response.msg);
                        });
                    }
                }, created: function () {
                    var url = '/blog/hotword/list/' + this.hashcode;
                    this.$http.get(url).then(function (response) {
                        this.blogList = response.data.data.blogList;
                        this.pageNum = response.data.data.pageNum;
                    }, function (response) {
                        console.log(response.msg);
                    });
                }
            };

            new Vue({
                el: '.main-panel',
                components: {
                    'blog-panel': blogPanel
                }
            });
        });
    </script>
    <style>
        .hotword-hl {
            font-weight: bold;
        }

        .summary {
            background-color: #d6e8c0;
            padding: 10px;
        }
    </style>
</head>
<body class="container">
    <div class="head-panel">
        <a class="btn btn-default" href="/index">首页</a>
    </div>
    <div class="main-panel">
        <div class="row">
            <blog-panel></blog-panel>
        </div>
    </div>
</body>
</html>
