<%@ page pageEncoding="UTF-8" %>
    <div class="float-nav hidden-xs hidden">
        <div class="list-group">
            <a class="list-group-item" href="/">首页</a>
            <a class="list-group-item" href="/cy/cyjl">成语接龙</a>
            <a class="list-group-item" href="#">****************</a>
            <a class="list-group-item" href="/logout">登出</a>
            <a class="list-group-item" href="/go/logon">登录</a>
            <a class="list-group-item" href="/admin/go/admin#/info">管理员</a>
            <a class="list-group-item" href="/buy">买(vue分页)</a>
            <a class="list-group-item" href="/buy/1">买(正常分页)</a>
        </div>
        <div class="min-float-nav-btn btn btn-danger">一</div>
    </div>
    <div class="max-float-nav-btn btn btn-success">口</div>
    <script>
        $(function () {
            $('.min-float-nav-btn').on('click', function () {
                $('.float-nav').addClass('hidden');
                $('.max-float-nav-btn').removeClass('hidden');
            });
            $('.max-float-nav-btn').on('click', function () {
                $('.float-nav').removeClass('hidden');
                $('.max-float-nav-btn').addClass('hidden');
            });
        });
    </script>
    <div class="row copyright">
        <p>Copyright © 2016-2017 - Powered by textBlog - Hosted by Xxxx - 商务合作 - </p>

        <p>最近访问人数{xxx},最后更新于2017年5月9日 20:33:50</p>
    </div>
    <div class="logon-mask hidden">
        <%--<form id="logon-panel" class="form-horizontal hidden" action="/login" method="post">--%>
        <div class="row">
            <div class="col-lg-4 col-lg-offset-4">
                <div id="logon-panel" class="form-horizontal" data-action="/ajaxlogin">
                    <div class="form-group">
                        <label for="username" class="col-sm-3 control-label">用户名</label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="username" id="username" value="user"
                                   placeholder="用户名">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-sm-3 control-label">密码</label>

                        <div class="col-sm-7">
                            <input type="password" class="form-control" name="password" id="password" value="user"
                                   placeholder="咒语">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-4 col-sm-8">
                            <button class="ajax-logon-btn btn btn-success">登 录</button>
                            <button class="cancel-logon-btn btn btn-danger">取消</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
