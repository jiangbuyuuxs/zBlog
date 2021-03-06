<%@ page import="java.security.Principal" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/3/5
  Time: 14:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = "anonymousUser";
    Principal userPrincipal = request.getUserPrincipal();
    if(userPrincipal!=null){
        username = userPrincipal.getName();
    }
    boolean remembered = SecurityUtils.getSubject().isRemembered();
%>
<html>
<head>
    <title>登录</title>
    <%@include file="comm/jscss.jsp" %>
    <link rel="stylesheet" href="/resources/css/logon.css">
    <script>
        $(function () {
           var logonUsername = "<%=username%>",remembered =<%=remembered%>;
            if(logonUsername!='anonymousUser'&&!remembered){
                $(".logined-username").text(logonUsername);
                $(".logon-panel").hide();
                $(".logined-panel").show();
            }else{
                $(".logined-panel").hide();
            }
            if(!!remembered){
                $('.remember-me-info').text('当前用户通过记住我登录,并不安全,所以需要二级验证');
            }

            $(".logout").one("click",function(){
                $.ajax("/logout",{
                    data:{isAjax:true},
                    type:"POST",
                    success: function () {
                        $(".logon-panel").show();
                        $(".logined-panel").hide();
                    }
                });
            });
            var message = "${errorMessage}";
            if(""!==message){
                BlogTool.alert(message);
            }
        });
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-lg-2 left-panel">
        </div>
        <div class="col-lg-6 logined-panel">
            <p>当前已登录,登录用户为<span class="logined-username"></span></p>
            <p>是否<a class="btn btn-danger btn-xs logout" href="#">退出</a>,或者<a class="btn btn-success btn-xs" href="javascript:history.go(-1);">返回</a></p>
        </div>
        <div class="col-lg-6 logon-panel">
            <form class="form-horizontal" action="/login" method="post">
                <div class="form-group">
                    <label for="username" class="col-sm-3 control-label">用户名</label>
                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="username" id="username" value="admin"
                               logoned="<%=username%>" placeholder="用户名">
                    </div>
                </div>
                <div class="form-group">
                    <label for="password" class="col-sm-3 control-label">密码</label>
                    <div class="col-sm-7">
                        <input type="password" class="form-control" name="password" id="password" value="admin" placeholder="咒语">
                        <a class="forget-pw" href="#"><del>忘记密码</del></a>
                    </div>
                </div>
                <div class="form-group hidden">
                    <div class="col-sm-offset-2 col-sm-10">
                        <input type="text" class="form-control" name="returnUrl" id="returnUrl"
                               value="${sessionScope.get("returnUrl")}">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-4">
                        <button type="submit" class="btn btn-success">登 录</button>
                        <input name="rememberMe" type="checkbox" />记住我(10天)
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <p></p>
                        <p class="remember-me-info"></p>
                    </div>
                </div>

            </form>
        </div>
        <div class="col-lg-2 right-panel">
            <div class="other-logon-list">
                <button class="btn btn-success disabled"><del>其他方式登录</del></button>
            </div>
        </div>
    </div>
<%@include file="comm/footer.jsp" %>
</div>
</body>
</html>
