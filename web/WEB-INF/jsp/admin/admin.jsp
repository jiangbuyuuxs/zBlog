<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/1
  Time: 22:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>后台<shiro:lacksRole name="admin">非管理员</shiro:lacksRole></title>
    <%@include file="../comm/jscss.jsp" %>
    <%@include file="../comm/vue.jsp" %>
    <script type="text/javascript" src="/resources/jQuery/plug/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="/resources/jQuery/plug/validate/messages_zh.js"></script>
    <style>
        body {
            background: #76c7ce;
        }

        .adminwin {
            border: 1px solid #dddddd;
            border-radius: 4px;
            padding: 20px;
        }

        .blog-list {
            min-height: 440px;
        }

        .blog-list .blog-item a {
            color: #000000;
            font-size: 14px;
            text-decoration: none;
        }

        .loading {
            text-align: center;
            position: absolute;
            top: 300px;
            left: 300px;
        }

        .add-blog {
            font-size: 12px;
        }

        #admin-manager {
            position: relative;
            top: 0px;
            min-height: 690px;
        }

        .add-user-panel {
            padding: 20px;
            background-color: #f8f8f8;
        }

        .logged-in-user-list {
            position: absolute;
            bottom: 0px;
            left: 0px;
        }

        .logged-user {
            padding: 3px;
            border: 1px solid #95ce95;
            border-radius: 3px;
            margin-left: 5px;
            background: #a1a5ce;
        }

        .user-info-panel {
            padding: 20px;
            background-color: #f8f8f8;
            min-height: 540px;
        }

        .head-image-container {
            width: 200px;
        }

        .head-image {
            width: 200px;
            height: 200px;
        }

        .head-image img {
            width: 200px;
            height: 200px;
        }

        .resume {
            min-height: 100px;
            border: 1px solid #cecece;
            border-radius: 3px;
            padding: 10px;
        }

        .file-upload-container {
            border-radius: 3px;
        }

        .file-upload-container input {
            background-color: #ffffff;
        }

        .buy-file-list {
            border: 1px solid #cecece;
            border-radius: 3px;
            padding: 20px;
        }

        .info-tab-panel, .blog-tab-panel {
            padding: 10px;
        }

        .blog-tab-panel > .row {
            border-bottom: 1px dashed #000000;
            margin: 0 0 5px 0;

        }

        .tab-container {
            background: #FFFFFF;
            height: 280px;
            border: 1px solid #dddddd;
            border-top: none;
        }

        .edit-user-panel {
            padding: 20px;
        }
    </style>
    <script type="text/x-template" id="head-nav-template">
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <span class="navbar-brand">哦,你好 <shiro:principal/></span>
                </div>
                <div class="navbar-form navbar-left">
                    <div class="form-group">
                        <input type="text" class="form-control search-keyword" placeholder="博文title搜索">
                    </div>
                    <button type="submit" class="btn btn-success" @click.prevent="search">搜索</button>
                </div>
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">操作 <span
                                class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a class="change-user" href="#">切换用户</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a href="/logout">退出</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </nav>
    </script>
    <script type="text/x-template" id="search-result-panel">
        <div class="search-result-panel">
            博文:
            <div class="blog-list" :class="{hidden:!hasBlogResult}">
                <table class="table">
                    <tbody>
                    <tr v-for="blog of blogList">
                        <td class="blog-item"><a :href="'/blog/blog/'+blog.id" target="_blank">{{blog.title}}</a></td>
                        <td>{{blog.editDate}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div :class="{hidden:hasBlogResult}">
                没有相关博文
            </div>
        </div>
    </script>
    <script type="text/x-template" id="menu-list-panel">
        <ul class="list-group">
            <li class="list-group-item">
                <router-link to="/info">博客信息</router-link>
            </li>
            <li class="list-group-item">
                <router-link to="/user/manager">用户管理</router-link>
            </li>
            <li class="list-group-item">
                <router-link to="/blog/manager">博文管理</router-link>
                <span class="add-blog">[<a href="/admin/blog/go/add" target="_blank">写一篇</a>]</span>
            </li>
            <li class="list-group-item">
                <router-link to="/buy/manager">淘宝客管理</router-link>
            </li>
            <li class="list-group-item">
                <router-link to="/todo/manager">Todo</router-link>
            </li>
        </ul>
    </script>
    <script type="text/x-template" id="blog-info-template">
        <table class="table">
            <tbody>
            <tr>
                <td>博文总数:</td>
                <td>{{info.blogCountNum}}</td>
                <td>博客总访问人次:</td>
                <td>{{info.visitCount}}</td>
            </tr>
            </tbody>
        </table>
    </script>
    <script type="text/x-template" id="user-manager-template">
        <div class="user-manager-panel">
            <div class="loading" v-if="loading">
                Loading...
            </div>
            <table class="table">
                <tbody>
                <shiro:hasRole name="admin">
                    <tr>
                        <td colspan="4"><a class="btn btn-success pull-left" href="#/user/add">添加用户</a></td>
                    </tr>
                </shiro:hasRole>
                <tr>
                    <th>操作</th>
                    <th>用户名</th>
                    <th>昵称</th>
                    <th width="150px">激活状态</th>
                </tr>
                <tr v-for="(user,index) of userList">
                    <td>
                        <shiro:hasRole name="admin">
                            <a class="btn btn-default btn-danger btn-xs"
                               :class="user.username==='admin'||user.username==='user'?'hidden':''"
                               @click.prevent="deleteUser(user.username)">删除</a>
                            <router-link :to="'/user/edit/'+user.username">
                                <a class="btn btn-default btn-success btn-xs"
                                   :class="user.username==='admin'||user.username==='user'?'hidden':''">编辑</a>
                            </router-link>
                        </shiro:hasRole>
                        <shiro:lacksRole name="admin">
                            <a class="btn btn-xs disabled" href="#" @click.prevent="">删除</a>
                            <a class="btn btn-xs disabled" href="#" @click.prevent="">编辑</a>
                        </shiro:lacksRole>
                    </td>
                    <td>{{user.username}}</td>
                    <td>{{user.nickname}}</td>
                    <td :class="user.enabled===1?'text-danger':'text-success'">
                        {{user.enabled===1?'激活':'未激活'}}
                        <shiro:hasRole name="admin">
                            <a class="btn btn-xs pull-right"
                               :class="[user.enabled===1?'btn-success':'btn-danger',user.username==='admin'||user.username==='user'?'hidden':'']"
                               @click.prevent="changeState(user.username,index)">
                                {{user.enabled===1?'禁用':'激活'}}
                            </a>
                        </shiro:hasRole>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </script>
    <script type="text/x-template" id="add-user-panel-template">
        <div class="add-user-panel">
            <form class="form-horizontal add-user-panel-form">
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="username">用户名:</label>

                    <div class="col-sm-4">
                        <input class="form-control" id="username" name="username" type="text" required/>
                    </div>
                    <div class="col-sm-4">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="password">密码:</label>

                    <div class="col-sm-4">
                        <input class="form-control" id="password" name="password" type="password" value="123456"/>
                    </div>
                    <div class="col-sm-4">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="password2">重复密码:</label>

                    <div class="col-sm-4">
                        <input class="form-control" id="password2" name="password2" type="password" value="123456"/>
                    </div>
                    <div class="col-sm-4">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="nickname">昵称:</label>

                    <div class="col-sm-4">
                        <input class="form-control" id="nickname" name="nickname" type="text" value="zbloguser"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="email">电子邮件:</label>

                    <div class="col-sm-4">
                        <input class="form-control" id="email" name="email" type="email" value="redis@zblog.com"
                               required/>
                    </div>
                    <div class="col-sm-4">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-6 control-label">
                        <a class="btn btn-danger" @click="back">返回</a>
                        <a class="btn btn-success submit" @click="addUser">添加用户</a>
                    </div>
                </div>
            </form>
        </div>
    </script>
    <script type="text/x-template" id="edit-user-panel-template">
        <div class="edit-user-panel">
            <form class="form-horizontal edit-user-panel-form">
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="username">用户名:</label>

                    <div class="col-sm-4">
                        <input class="form-control" readonly id="username" name="username" type="text"
                               :value="userInfo.username"/>
                    </div>
                    <div class="col-sm-4">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="nickname">昵称:</label>

                    <div class="col-sm-4">
                        <input class="form-control" id="nickname" name="nickname" type="text"
                               :value="userInfo.nickname"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-6 control-label">
                        <a class="btn btn-danger" @click="back">返回</a>
                        <a class="btn btn-success submit" @click="editUser">修改</a>
                    </div>
                </div>
            </form>
        </div>
    </script>
    <script type="text/x-template" id="blog-manager-template">
        <div>
            <div class="loading" v-if="loading">
                Loading...
            </div>
            <div class="blog-list">
                <table class="table">
                    <tbody>
                    <tr>
                        <th width="10%">操作</th>
                        <th width="70%">标题</th>
                        <th width="20%">最后修改时间</th>
                    </tr>
                    <tr v-for="blog of blogList">
                        <td>
                            <shiro:hasRole name="admin">
                                <a class="btn btn-default btn-danger btn-xs" href="#"
                                   @click.prevent="deleteBlog(blog.id)">删除</a>
                                <a class="btn btn-default btn-success btn-xs" :href="'/admin/blog/go/edit/'+blog.id"
                                   target="_blank">编辑</a>
                            </shiro:hasRole>
                            <shiro:lacksRole name="admin">
                                <a class="btn btn-default btn-xs disabled" href="#" @click.prevent="">删除</a>
                                <a class="btn btn-default btn-xs disabled" href="#" @click.prevent="">编辑</a>
                            </shiro:lacksRole>
                        </td>
                        <td class="blog-item"><a :href="'/blog/blog/'+blog.id" target="_blank">{{blog.title}}</a></td>
                        <td>{{blog.editDate}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="page-bar" :class="{hidden:pageNum<2}">
                <ul class="pagination">
                    <li><a href="#" @click.prevent="fetchData(curPage-1)"><span>&laquo;</span></a></li>
                    <li v-for="i in pageNum" :class="{active:i===curPage}"><a href="#" @click.prevent="fetchData(i)"
                                                                              :data-page="i">{{i}}</a></li>
                    <li><a href="#" @click.prevent="fetchData(curPage+1)"><span>&raquo;</span></a></li>
                </ul>
            </div>
        </div>
    </script>
    <script type="text/x-template" id="logged-in-user-list-template">
        <div class="logged-in-user-list hidden-xs">
            <span>当前访问人数:{{loggedInUserCount}}</span>
            <span>当前已登录用户:</span>
            <span v-for="loggedInUser of loggedInUserList">
                <router-link :to="'/user/info/'+loggedInUser"><a class="logged-user">{{loggedInUser}}</a>
                    <%--<router-link :to="{name:'userInfoPanel',params:{username:loggedInUser}}"><a class="logged-user">{{loggedInUser}}</a>--%>
                </router-link>
            </span>
        </div>
    </script>
    <script type="text/x-template" id="user-info-panel-template">
        <div class="user-info-panel">
            <div class="row">
                <div class="col-lg-4">
                    <div class="head-image-container">
                        <div class="head-image">
                            <img src="http://78rcwc.com1.z0.glb.clouddn.com/image/lvcheng/CSC_0006_2?imageslim"/>
                        </div>
                        <div class="username text-center">
                            {{userInfo.username}}
                        </div>
                        <div class="row text-center">
                            <a @click="back" class="btn btn-danger btn-xs">返回</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-8 col-lg-8">
                    <p class="resume">这是一个奇怪的人,什么都没有留下.</p>
                    <ul class="nav nav-tabs tab-head">
                        <li class="info-li active"><a href="#" @click.prevent="togglePanel('info')">个人信息</a></li>
                        <li class="blog-li"><a href="#" @click.prevent="togglePanel('blog')">某人的博客</a></li>
                        <li class="topic-li"><a href="#" @click.prevent="togglePanel('topic')">某人的话题</a></li>
                    </ul>
                    <div class="tab-content tab-container">
                        <components :is="currentView" :user-info="userInfo"></components>
                    </div>
                </div>
            </div>
        </div>
    </script>
    <script type="text/x-template" id="info-tab-panel-template">
        <div class="info-tab-panel tab-panel">
            <div class="row">
                <div class="col-lg-2">昵称:</div>
                <div class="col-lg-4">{{userInfo.nickname}}<a class="btn btn-success btn-xs" href="#"
                                                              @click.prevent="sendMessage(userInfo.username)">发消息</a>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-2">电子邮件:</div>
                <div class="col-lg-4">{{userInfo.email}}</div>
            </div>
        </div>
    </script>
    <script type="text/x-template" id="blog-tab-panel-template">
        <div class="blog-tab-panel tab-panel">
            <div class="row" v-for="blog of userBlogList">
                <div class="col-lg-6">
                    <a target="_blank" :href="'/detail/'+blog.id+'/id'">{{blog.title}}</a>
                </div>
                <div class="col-lg-3 pull-right">
                    {{blog.createDate}}
                </div>
            </div>
        </div>
    </script>
    <script type="text/x-template" id="topic-tab-panel-template">
        <div class="topic-tab-panel tab-panel">
            未实现
        </div>
    </script>
    <script type="text/x-template" id="buy-manager-panel-template">
        <div class="buy-manager-panel">
            <div class="file-upload-container">
                <form id="buyFileFrom">
                    <input type="file" name="buyFile" id="buyFile"/>
                    <a class="btn btn-xs btn-success" @click.prevent="upload">上传</a>
                    <span class="text-danger">限制40M以内(.xls)文件</span>
                </form>
            </div>
            <div>已完成:<span class="percent">0</span>%</div>
            <ul class="buy-file-list">
                <li v-for="fileName in fileList">
                    {{fileName}} <a class="btn btn-success btn-xs" @click.prevent="parseFile(fileName)">解析</a> <a
                        class="btn btn-danger btn-xs" @click.prevent="deleteFile(fileName)">删除</a>
                </li>
            </ul>
        </div>
    </script>
    <script type="text/x-template" id="todo-panel-template">
        <div class="todo-panel">
            <table class="table table-condensed">
                <tbody>
                <tr>
                    <th class="col-lg-2">操作</td>
                    <th class="col-lg-6">标题</td>
                    <th class="col-lg-2">日期</td>
                    <th class="col-lg-2">状态</td>
                </tr>
                <tr v-for="todo in todoList">
                    <td>
                        <a class="btn btn-xs btn-success">完成</a>
                        <a class="btn btn-xs btn-danger">删除</a>
                    </td>
                    <td>{{todo.title}}</td>
                    <td>{{todo.createDate}}</td>
                    <td>{{todo.state}}</td>
                </tr>
                </tbody>
            </table>
            <div>
                <div class="form-group">
                    <input class="form-control todo-title" name="title" placeholder="标题">
                </div>
                <div class="form-group">
                    <textarea class="form-control todo-remark" name="remark" placeholder="内容"></textarea>
                </div>
                <div class="form-group">
                    <a @click.prevent="addTodo" class="btn btn-success" :class="{addBtn:disable}">添加</a>
                </div>
            </div>
        </div>
    </script>
    <script>
        $(function () {
            Vue.http.interceptors.push(function (request, next) {
                next(function (response) {
                    if (!BlogTool.checkLogin(response)) {
                        //返回登录超时
                        response.ok = false;
                    }
                });
            });

            var headNavPanel = {
                template: '#head-nav-template',
                methods: {
                    search: function () {
                        var keyword = $('.search-keyword').val();
                        if (keyword != '' && keyword != undefined) {
                            this.$router.push('/search/result/:' + keyword);
                        }
                    }
                }
            };
            var searchResultPanel = {
                template: '#search-result-panel',
                data: function () {
                    return {
                        blogList: []
                    }
                },
                methods: {
                    fetchData: function () {
                        var keyword = $('.search-keyword').val();
                        var url = '/admin/search/search';
                        this.$http.post(url, {
                            keyword: keyword,
                            page: 1,
                            pageSize: 5
                        }, {emulateJSON: true}).then(function (response) {
                            this.blogList = response.data.data.blogList;
                        });
                    }
                },
                computed: {
                    hasBlogResult:function () {
                        return this.blogList.length != 0;
                    }

                },
                beforeRouteUpdate: function (to, from, next) {
                    this.fetchData();
                },
                created: function () {
                    this.fetchData();
                }
            };

            var menuListPanel = {
                template: '#menu-list-panel'
            };

            var infoPanel = {
                template: '#blog-info-template',
                data: function () {
                    return {
                        info: {},
                        loading: false
                    };
                },
                methods: {
                    fetchData: function (page) {
                        this.loading = true;
                        var url = '/admin/blogInfo';
                        this.$http.get(url).then(function (response) {
                            if (!BlogTool.checkLogin(response))
                                return;
                            this.info = response.data.data;
                            this.loading = false;
                        }, function (response) {
                            this.loading = false;
                        });
                    }
                },
                created: function () {
                    this.fetchData();
                }
            };

            var userManagerPanel = {
                template: '#user-manager-template',
                data: function () {
                    return {
                        userList: [],
                        curPage: 1,
                        loading: false
                    };
                },
                methods: {
                    fetchData: function (page) {
                        this.loading = true;
                        var url = '/admin/user/list';
                        this.$http.get(url).then(function (response) {
                            if(response.data.success){
                                this.loading = false;
                                this.userList = response.data.data.userList;
                                this.curPage = page;
                            }else{
                                BlogTool.alert(response.data.message);
                                this.loading = false;
                            }
                        }, function (response) {
                            this.loading = false;
                            console.log(response);
                        });
                    },
                    deleteUser: function (username) {
                        if (confirm('是否删除该用户(' + username + ')')) {
                            var url = '/admin/user/delete';
                            this.$http.get(url, {
                                params: {
                                    username: username,
                                    page: this.curPage
                                }
                            }).then(function (response) {
                                if(response.data.success){
                                    this.userList = response.data.data.userList;
                                }else{
                                    BlogTool.alert(response.data.message);
                                }
                            }, function (response) {
                                console.log(response.data.message);
                            });
                        }
                    },
                    changeState: function (username, index) {
                        var url = '/admin/user/enabled';
                        this.$http.get(url, {
                            params: {
                                username: username
                            }
                        }).then(function (response) {
                            if (response.data.success)
                                this.userList[index].enabled = response.data.data.enabled;
                            else
                                BlogTool.alert(response.data.message);
                        });
                    }
                },
                created: function () {
                    this.fetchData(1);
                }
            };
            var addUserPanel = {
                template: '#add-user-panel-template',
                methods: {
                    addUser: function () {
                        var valid = $(".add-user-panel-form").valid();
                        if (valid) {
                            var data = {
                                username: $('#username').val(),
                                password: $('#password').val(),
                                nickname: $('#nickname').val(),
                                email: $('#email').val()
                            };
                            var url = '/admin/user/add';
                            this.$http.post(url, data, {
                                emulateJSON: true
                            }).then(function (response) {
                                if (response.data.success) {
                                    this.$router.push('/user/manager');
                                } else {
                                    BlogTool.alert(response.data.message);
                                }
                            });

                        }
                    },
                    back: function () {
                        this.$router.go(-1);
                    }
                },
                created: function () {
                    //这里仍未渲染
                    this.$nextTick(
                            function () {
                                $(".add-user-panel-form").validate({
                                    debug: true,
                                    errorPlacement: function (error, element) {
                                        error.appendTo(element.parent("div").next("div"));
                                    },
                                    errorElement: "span",
                                    rules: {
                                        username: {
                                            remote: {
                                                url: "/admin/user/exist",
                                                type: "post",
                                                dataType: "json"
                                            }
                                        },
                                        password2: {
                                            required: true,
                                            equalTo: "#password"
                                        }
                                    },
                                    messages: {
                                        username: {
                                            remote: "当前用户名已存在"
                                        },
                                        password2: {
                                            required: "必填",
                                            equalTo: "与密码不一致"
                                        }
                                    }
                                });
                            }
                    );
                }
            };
            var editUserPanel = {
                template: '#edit-user-panel-template',
                data: function () {
                    return {
                        userInfo: {}
                    }
                },
                methods: {
                    editUser: function () {
                        var data = {
                            username: this.userInfo.username,
                            nickname: $('#nickname').val()
                        };
                        var url = '/admin/user/edit';
                        this.$http.post(url, data, {
                            emulateJSON: true
                        }).then(function (response) {
                            if (response.data.success) {
                                this.$router.push('/user/manager');
                            } else {
                                BlogTool.alert(response.data.message);
                                this.fetchData();
                            }
                        });
                    },
                    back: function () {
                        this.$router.go(-1);
                    },
                    fetchData: function () {
                        var username = this.$route.params.username;
                        var url = '/admin/user/username/' + username;
                        this.$http.get(url).then(function (response) {
                            if (response.data.success) {
                                this.userInfo = response.data.data.userInfo;
                            } else {
                                BlogTool.alert(response.data.message);
                            }
                        });
                    }
                },
                created: function () {
                    this.fetchData();
                }
            };
            var blogPageSize = 10;
            var blogManagerPanel = {
                        template: '#blog-manager-template',
                        data: function () {
                            return {
                                blogList: [],
                                blogCountNum: 0,
                                pageNum: 0,
                                curPage: 0,
                                loading: false
                            };
                        },
                        methods: {
                            fetchData: function (page, init) {
                                if (!init)
                                    if (page < 1 || page > this.pageNum || page === this.curPage)
                                        return;
                                this.loading = true;
                                var url = '/blog/list';
                                this.$http.get(url, {
                                    params: {
                                        page: page,
                                        pageSize: blogPageSize
                                    }
                                }).then(function (response) {
                                    BlogTool.checkLogin(response);
                                    this.loading = false;
                                    this.blogList = response.data.data.blogList;
                                    this.blogCountNum = response.data.data.blogCountNum;
                                    this.pageNum = response.data.data.pageNum;
                                    this.curPage = page;
                                }, function (response) {
                                    this.loading = false;
                                    BlogTool.alert(response.message);
                                });
                            },
                            deleteBlog: function (id) {
                                var sure = confirm('是否删除该博文(id为:' + id + ')');
                                if (sure) {
                                    var url = '/admin/blog/delete';
                                    this.$http.get(url, {
                                        params: {
                                            id: id,
                                            page: this.curPage
                                        }
                                    }).then(function (response) {
                                        this.blogList = response.data.data.blogList;
                                        this.pageNum = response.data.data.pageNum;
                                        if (this.blogList.length == 0) {
                                            this.fetchData(this.curPage - 1, false);
                                        }
                                    }, function (response) {
                                        BlogTool.alert(response.message);
                                    });
                                }
                            }
                        },
                        created: function () {
                            this.fetchData(1, true);
                        }
                    }
                    ;

            var loggedInUserList = {
                template: '#logged-in-user-list-template',
                data: function () {
                    return {
                        loggedInUserList: [],
                        loggedInUserCount: 0
                    }
                },
                methods: {
                    fetchData: function () {
                        var url = '/admin/loggeduser';
                        this.$http.get(url).then(function (response) {
                                    this.loggedInUserList = response.data.data.loggedInUserList;
                                    this.loggedInUserCount = response.data.data.loggedInUserCount;
                                }, function (response) {

                                }
                        );
                    }
                },
                created: function () {
                    this.fetchData();
                }
            };

            var infoTabPanel = {
                template: '#info-tab-panel-template',
                props: ['userInfo'],
                methods: {
                    sendMessage: function (username) {
                        BlogTool.sendMessage(username);
                    }
                }
            };
            var blogTabPanel = {
                template: '#blog-tab-panel-template',
                props: ['userInfo'],
                data: function () {
                    return {userBlogList: []}
                },
                methods: {
                    fetchData: function () {
                        var username = this.userInfo.username;
                        var url = '/admin/blog/list/username/' + username;
                        this.$http.get(url).then(function (response) {
                                    if (response.data.success)
                                        this.userBlogList = response.data.data.userBlogList;
                                }, function (response) {

                                }
                        );
                    }
                },
                watch: {
                    'userInfo': function () {
                        this.fetchData();
                    }
                },
                created: function () {
                    this.fetchData();
                }
            };
            var topicTabPanel = {
                template: '#topic-tab-panel-template'
            };
            var userInfoPanel = {
                        template: '#user-info-panel-template',
                        data: function () {
                            return {
                                userInfo: {},
                                currentView: infoTabPanel
                            }
                        },
                        components: {
                            'infoTabPanel': infoTabPanel,
                            'blogTabPanel': blogTabPanel,
                            'topicTabPanel': topicTabPanel
                        },
                        methods: {
                            togglePanel: function (id) {
                                this.currentView = id + 'TabPanel';
                                $('.tab-head li').removeClass('active');
                                $('.' + id + '-li').addClass('active');
                            },
                            fetchData: function (username) {
                                var url = '/admin/user/username/' + username;
                                this.$http.get(url).then(function (response) {
                                            if (response.data.success) {
                                                this.userInfo = response.data.data.userInfo;
                                            }
                                        }, function (response) {

                                        }
                                );
                            },
                            back: function () {
                                this.$router.go(-1);
                            },
                            getUsername: function () {
                                var username = this.$route.params.username;
                                if (username !== undefined) {
                                    BlogTool.setCookie("userinfo:username", username, 1);
                                } else {
                                    username = BlogTool.getCookie("userinfo:username");
                                }
                                if (username === undefined) {
                                    router.push("/");
                                }
                                return username;
                            }
                        },
                        watch: {
                            '$route.params': function (params) {
                                console.log(params);
                            },
                            '$route': function (to, from) {
                                //监听的路由变化,但是无法监听使用param传递的
                                this.fetchData(this.getUsername());
                            }
                        },
                        created: function () {
                            this.fetchData(this.getUsername());
                        }
                    }
                    ;
            var buyManagerPanel = {
                        template: '#buy-manager-panel-template',
                        data: function () {
                            return {
                                fileList: [],
                                uploading: false
                            }
                        },
                        methods: {
                            fetchData: function () {
                                var url = '/admin/buy/file/list';
                                this.$http.get(url).then(function (response) {
                                            if (response.data.success)
                                                this.fileList = response.data.data.fileList;
                                        }, function (response) {

                                        }
                                );
                                this.$nextTick(function () {
                                    $('.percent').parent().hide();
                                })
                            },
                            upload: function () {
                                var formData = new FormData($('#buyFileFrom')[0]);
                                var url = '/admin/buy/file/upload';
                                var percentObj = $('.percent');
                                this.$http.post(url, formData, {
                                    progress: function (event) {
                                        if (!this.uploading) {
                                            percentObj.parent().show();
                                        }
                                        percentObj.text(Math.floor((event.loaded / event.total) * 100));
                                        this.uploading = true;
                                    }
                                }).then(function (response) {
                                            percentObj.parent().hide();
                                            this.uploading = false;
                                            var file = $('#buyFile')[0];
                                            file.outerHTML = file.outerHTML
                                            if (response.data.success) {
                                                this.fileList = response.data.data.fileList;
                                            } else {
                                                alert(response.data.message ? response.data.message : '上传失败');
                                            }
                                        }, function (response) {

                                        }
                                );

                            },
                            parseFile: function (fileName) {
                                var url = '/admin/buy/file/parse';
                                this.$http.post(url, {fileName: fileName},
                                        {
                                            emulateJSON: true
                                        }
                                ).
                                        then(function (response) {
                                            if (response.data.success) {
                                                alert('解析成功');
                                            } else {
                                                alert(response.data.data.message ? response.data.data.message : '解析失败');
                                            }
                                        }, function (response) {

                                        }
                                );
                            },
                            deleteFile: function (fileName) {
                                var isDel = confirm('是否删除');
                                if (!isDel) {
                                    return false;
                                }
                                var url = '/admin/buy/file/delete';
                                this.$http.post(url, {fileName: fileName},
                                        {
                                            emulateJSON: true
                                        }
                                ).
                                        then(function (response) {
                                            if (response.data.success) {
                                                alert(response.data.message);
                                                this.fetchData();
                                            } else {
                                                alert(response.data.message);
                                            }
                                        }, function (response) {

                                        }
                                );
                            }
                        },
                        created: function () {
                            this.fetchData();
                        }
                    }
                    ;

            var todoPanel = {
                template:'#todo-panel-template',
                data: function () {
                  return {
                      todoList:'',
                      addBtn:true
                  }
                },
                methods:{
                    fetchData: function () {
                        var url = '/admin/todo/list';
                        this.$http.post(url).
                                then(function (response) {
                                    if (response.data.success) {
                                        this.todoList = response.data.data.todoList;
                                    } else {
                                        alert(response.data.message);
                                    }
                                }, function (response) {

                                }
                        );
                    },addTodo: function () {
                        this.addBtn = false;
                        var url = '/admin/todo/add';
                        var title = $('.todo-title').val();
                        var remark = $('.todo-remark').val();
                        this.$http.post(url, {title: title,remark:remark},
                                {
                                    emulateJSON: true
                                }).
                                then(function (response) {
                                    if (response.data.success) {
                                        this.todoList = response.data.data.todoList;
                                    } else {
                                        alert(response.data.message);
                                    }
                                    this.addBtn = true;
                                }, function (response) {

                                }
                        );
                    }
                },
                created: function () {
                    this.fetchData();
                }
            };

            new Vue({
                        el: "#admin-manager",
                        components: {
                            'head-nav-panel': headNavPanel,
                            'search-result-panel': searchResultPanel,
                            'info-panel': infoPanel,
                            'user-manager-panel': userManagerPanel,
                            'add-user-panel': addUserPanel,
                            'edit-user-panel': editUserPanel,
                            'user-info-panel': userInfoPanel,
                            'blog-manager-panel': blogManagerPanel,
                            'buy-manager-panel': buyManagerPanel,
                            'menu-list': menuListPanel,
                            'logged-in-user-list': loggedInUserList,
                            'todo-panel': todoPanel
                        },
                        router: new VueRouter({
                            routes: [
                                {path: '/info', component: infoPanel},
                                {path: '/', component: infoPanel},
                                {path: '/user/manager', component: userManagerPanel},
                                {path: '/user/add', component: addUserPanel},
                                {path: '/user/edit/:username', component: editUserPanel},
                                {path: '/user/info/:username', component: userInfoPanel, name: 'userInfoPanel'},
                                {path: '/blog/manager', component: blogManagerPanel},
                                {path: '/search/result/:keyword', component: searchResultPanel},
                                {path: '/buy/manager', component: buyManagerPanel},
                                {path: '/todo/manager', component: todoPanel}
                            ]
                        })
                    }
            );
        })
        ;
    </script>
</head>
<body>
<div class="container">
    <div id="admin-manager">
        <div class="row">
            <head-nav-panel></head-nav-panel>
        </div>
        <div class="row">
            <div class="col-sm-2">
                <menu-list></menu-list>
            </div>
            <div class="col-sm-10 adminwin">
                <router-view></router-view>
            </div>
        </div>
        <logged-in-user-list></logged-in-user-list>
    </div>
    <%@include file="../comm/footer.jsp" %>
</div>
</body>
</html>
