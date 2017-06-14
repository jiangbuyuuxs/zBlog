<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>热词搜索页</title>
    <%@include file="comm/jscss.jsp" %>
    <link rel="stylesheet" type="text/css" href="/resources/css/index.css">
    <script>
        $(function () {
            var pageSize = '${pageSize}';
            var blogCountNum = '${blogCountNum}';
            var pageNum = Math.ceil(blogCountNum / pageSize);
            var hashcode = '${hashcode}';
            var blogPanel = new Vue({
                el: '.main-panel',
                data: {
                    blogList:${blogList},
                    pageNum: pageNum,
                    curPage: 1
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
                        if (page < 1 || page > pageNum || page === this.curPage)
                            return
                        var url = '/hotword/'+hashcode+'/id/' + page + '/page/'+pageSize+'/pagesize';
                        this.$http.get(url).then(function(data){
                            this.blogList = data.data.blogList;
                            this.curPage = page;
                        },function(data){
                            console.log(data.msg);
                        });
                    }
                }
            });
        });
    </script>
</head>
<body class="container">
<div class="head-panel">

</div>
<div class="main-panel">
    <div class="row">
        <div class="col-lg-9 col-md-8 col-sm-12 col-xs-12 blog-panel">
            <div class="row blog-list-head">
                <div class="col-lg-10 col-md-9 col-sm-9 col-xs-8 title">标题</div>
                <div class="col-lg-2 col-md-3 col-sm-3 col-xs-4">发布时间</div>
            </div>
            <div class="blog-list">
                <div v-for="(blog,index) in blogList" class="row">
                    <div class="col-lg-10 col-md-9 col-sm-9 col-xs-8 padding2px"><a :title="blog.title"
                                                                                    :href="'/detail/'+blog.id+'/id'">{{blog.title}}</a>
                    </div>
                    <div class="col-lg-2 col-md-3 col-sm-3 col-xs-4 padding2px">{{blog.createDate}}</div>
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
    </div>
</div>
</body>
</html>
