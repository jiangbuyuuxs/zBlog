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
    <script type="text/javascript" src="/resources/jQuery/plug/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="/resources/jQuery/plug/validate/messages_zh.js"></script>
    <style>
        body {
            background: #76c7ce;
        }

        .adminwin {
            border: 1px solid #dddddd;
            border-radius: 4px;
        }

        .blog-list {
            height: 430px;
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
            min-height: 700px;
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
            margin: 20px 0;
            background-color: #f8f8f8;
            min-height: 560px;
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
    </style>
    <script type="text/x-template" id="blog-info-template">
        <table class="table">
            <tbody>
            <tr>
                <td>博文总数</td>
                <td>{{info.blogCountNum}}</td>
                <td>总访问人数</td>
                <td>{{info.visitCount}}</td>
            </tr>
            </tbody>
        </table>
    </script>
    <script type="text/x-template" id="user-manager-template">
        <table class="table">
            <tbody>
            <tr>
                <th>操作</th>
                <th>用户名</th>
                <th>昵称</th>
                <th>激活状态</th>
            </tr>
            <div class="loading" v-if="loading">
                Loading...
            </div>
            <tr v-for="user of users">
                <td>
                    <shiro:hasRole name="admin">
                        <a class="btn btn-default btn-xs" href="#" @click.prevent="deleteUser(user.username)">删除</a>
                        <a class="btn btn-default btn-xs" href="#" @click.prevent="editUser(user.username)">编辑</a>
                    </shiro:hasRole>
                    <shiro:lacksRole name="admin">
                        <a class="btn btn-default btn-xs disabled" href="#" @click.prevent="">删除</a>
                        <a class="btn btn-default btn-xs disabled" href="#" @click.prevent="">编辑</a>
                    </shiro:lacksRole>
                </td>
                <td>{{user.username}}</td>
                <td>{{user.nickname}}</td>
                <td :class="user.enabled===1?'text-danger':'text-success'">{{user.enabled===1?'激活':'未激活'}}</td>
            </tr>
            <shiro:hasRole name="admin">
                <tr>
                    <td colspan="4"><a class="btn btn-success btn-xs pull-right" href="#/user/add">添加</a></td>
                </tr>
            </shiro:hasRole>
            </tbody>
        </table>
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
                        <a class="btn btn-success" @click="back">返回</a>
                        <a class="btn btn-success submit" @click="addUser">添加用户</a>
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
                                <a class="btn btn-default btn-xs" href="#" @click.prevent="deleteBlog(blog.id)">删除</a>
                                <a class="btn btn-default btn-xs" :href="'/admin/blog/'+blog.id+'/edit'"
                                   target="_blank">编辑</a>
                            </shiro:hasRole>
                            <shiro:lacksRole name="admin">
                                <a class="btn btn-default btn-xs disabled" href="#" @click.prevent="">删除</a>
                                <a class="btn btn-default btn-xs disabled" href="#" @click.prevent="">编辑</a>
                            </shiro:lacksRole>
                        </td>
                        <td><a :href="'/detail/'+blog.id+'/id'" target="_blank">{{blog.title}}</a></td>
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
        <div class="logged-in-user-list">
            <span>当前登录用户数:{{loggedInUserCount}}</span>
            <span>当前登录用户:</span>
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
                        <div>
                            <a @click="back" class="btn btn-success">返回</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-8 col-lg-8">
                    <p class="resume">这是一个奇怪的人,什么都没有留下.</p>
                    <span>个人信息</span>
                    <span>博客</span>
                    <span>话题</span>

                    <div>
                        <ul>
                            <li>昵称:{{userInfo.nickname}}</li>
                            <li>电子邮件:{{userInfo.email}}</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </script>
    <script>
        $(function () {
            Vue.component('info-panel', {
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
                        this.$http.get(url).then(function (data) {
                            BlogTool.checkLogin(data);
                            this.info = data.data;
                            this.loading = false;
                        }, function (response) {
                            this.loading = false;
                            console.log(data.msg);
                        });
                    }
                },
                created: function () {
                    this.fetchData();
                }
            });

            Vue.component('user-manager-panel', {
                template: '#user-manager-template',
                data: function () {
                    return {
                        users: [],
                        curPage: 1,
                        loading: false
                    };
                },
                methods: {
                    fetchData: function (page) {
                        this.loading = true;
                        var url = '/admin/user/' + page + '/page';
                        this.$http.get(url).then(function (data) {
                            BlogTool.checkLogin(data);
                            this.loading = false;
                            this.users = data.data;
                            this.curPage = page;
                        }, function (response) {
                            this.loading = false;
                            console.log(data.msg);
                        });
                    },
                    deleteUser: function (username) {
                        if (confirm('是否删除该用户(' + username + ')')) {
                            var url = '/admin/user/' + username + '/del/' + this.curPage + '/page';
                            this.$http.get(url).then(function (data) {
                                this.users = data.data;
                            }, function (response) {
                                console.log(data.msg);
                            });
                        }
                    }
                },
                created: function () {
                    this.fetchData(1);
                }
            })
            ;
            Vue.component('add-user-panel', {
                template: '#add-user-panel-template',
                data: function () {
                    return {
                        users: []
                    };
                },
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
                            this.$http.post(url, {
                                emulateJSON: true,
                                data: data
                            }).then(function (data) {
                                if (data.success) {
                                    //
                                    router.push('/user/manager');
                                } else {

                                }
                            });

                        }
                    },
                    back: function () {
                        router.go(-1);
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
            })
            ;
            var blogPageSize = 10;
            Vue.component('blog-manager-panel', {
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
                        var url = '/admin/blog/' + page + '/page/' + blogPageSize + '/pagesize';
                        this.$http.get(url).then(function (data) {
                            BlogTool.checkLogin(data);
                            this.loading = false;
                            this.blogList = data.data.blogList;
                            this.blogCountNum = data.data.blogCountNum;
                            this.pageNum = Math.ceil(this.blogCountNum / blogPageSize);
                            this.curPage = page;
                        }, function (response) {
                            this.loading = false;
                            console.log(data.msg);
                        });
                    },
                    deleteBlog: function (id) {
                        var sure = confirm('是否删除该博文(id为:' + id + ')');
                        if (sure) {
                            var url = '/admin/blog/' + id + '/del/' + this.curPage + '/page';
                            this.$http.get(url).then(function (data) {
                                this.blogList = data.data.blogList;
                                this.pageNum = Math.ceil(this.blogCountNum / 10);
                                if (this.blogList.length == 0) {
                                    this.fetchData(this.curPage - 1, false);
                                }
                            }, function (response) {
                                console.log(data.msg);
                            });
                        }
                    }
                },
                created: function () {
                    this.fetchData(1, true);
                }
            })
            ;

            Vue.component('logged-in-user-list', {
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
                        this.$http.get(url).then(function (data) {
                                    this.loggedInUserList = data.data.loggedInUserList;
                                    this.loggedInUserCount = data.data.loggedInUserCount;
                                }, function (data) {

                                }
                        );
                    }
                },
                created: function () {
                    this.fetchData();
                }
            })
            ;
            Vue.component('user-info-panel', {
                template: '#user-info-panel-template',
                data: function () {
                    return {
                        userInfo: {}
                    }
                },
                methods: {
                    fetchData: function (username) {
                        var url = '/admin/user/' + username + '/userinfo';
                        this.$http.get(url).then(function (data) {
                                    if (data.data.success) {
                                        this.userInfo = data.data.userInfo;
                                    }
                                }, function (data) {

                                }
                        );
                    },
                    back: function () {
                        console.log("返回");
                        router.go(-1);
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
            })
            ;

            var infoPanel = Vue.component('info-panel');
            var blogManager = Vue.component('blog-manager-panel');
            var userManager = Vue.component('user-manager-panel');
            var addUserPanel = Vue.component('add-user-panel');
            var userInfoPanel = Vue.component('user-info-panel');

            var router = new VueRouter({
                routes: [
                    {path: '/info', component: infoPanel},
                    {path: '/', component: infoPanel},
                    {path: '/user/manager', component: userManager},
                    {path: '/user/add', component: addUserPanel},
                    {name: 'userInfoPanel',path: '/user/info/:username', component: userInfoPanel},
                    {path: '/blog/manager', component: blogManager}
                ]
            });

            new Vue({
                        el: "#admin-manager",
                        router: router
                    }
            );
        })
        ;
    </script>
</head>
<body class="container">
<div id="admin-manager">
    <div class="row">
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <span class="navbar-brand">哦,<shiro:principal/></span>
                </div>
                <div class="navbar-form navbar-left">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">搜索</button>
                </div>
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">操作 <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="#">掘金</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a href="/logout">退出</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </nav>
    </div>
    <div class="row">
        <div class="col-sm-2">
            <ul class="list-group">
                <li class="list-group-item">
                    <router-link to="/info">博客信息管理</router-link>
                </li>
                <li class="list-group-item">
                    <router-link to="/user/manager">用户管理</router-link>
                </li>
                <li class="list-group-item">
                    <router-link to="/blog/manager">博文管理</router-link>
                    <span class="add-blog">[<a href="/admin/blog/go/add" target="_blank">写一篇</a>]</span>
                </li>
            </ul>
        </div>
        <div class="col-sm-10 adminwin">
            <router-view></router-view>
        </div>
    </div>
    <logged-in-user-list></logged-in-user-list>
</div>
<%@include file="../comm/footer.jsp" %>
</body>
</html>
