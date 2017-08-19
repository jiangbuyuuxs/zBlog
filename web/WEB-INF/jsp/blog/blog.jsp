<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/1
  Time: 18:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <!--因为知乎有防盗链,所以如果发送referrer就直接403-->
    <meta name="referrer" content="never">
    <title>博文显示</title>
    <%@include file="../comm/jscss.jsp" %>
    <style>
        body {
            background: #353535;
        }

        .highlight {
            padding: 9px 14px;
            margin-bottom: 14px;
            background-color: #f7f7f9;
            border: 1px solid #e1e1e8;
            border-radius: 4px;
            min-height: 550px;
        }

        .highlight img {
            display: block;
            max-width: 100%;
        }

        .blog-title {
            text-align: center;
            min-height: 60px;
            line-height: 60px;
        }

        .go-top-btn {
            width: 50px;
            height: 50px;
            display: block;
            background: url("/resources/img/go-top.png") no-repeat;
        }

        .tool-bar {
            position: fixed;
            right: 40px;
            bottom: 40px;
        }

        .comment li {
            padding: 2px 0;
        }

        .focus-comment {
            background: #99dda7;
        }

        .reply-container {
            border-top: 1px dotted #000;
            margin: 10px;
            padding: 10px;
        }
    </style>
    <script>
        $(function () {
           //加载工具条
            initToolBar();
            //加载评论
            initComment();


            function initToolBar(){
                $('#go-top').on('click', function () {
                    $('body').animate({
                        scrollTop: 0
                    }, {
                        duration: 500,
                        easing: 'linear'
                    });
                });
                $(window).on('scroll', function () {
                    if ($('body').scrollTop() > 0) {
                        $('.tool-bar').removeClass('hidden');
                    } else {
                        $('.tool-bar').addClass('hidden');
                    }
                });
            }

            function initComment(){
                function createComment(comment, level) {
                    var result = '<li class="comment-level-' + (level++) + '">' +
                            '<a href="/user/userinfo/' + comment.uId + '" target="_blank">' + comment.username + '</a> ' + comment.content +
                            ' ' + getDescriptionLeaveNow(comment.cTime) +
                            '<span class=" hidden"> ' +
                            '<a class="up-down-btn" data-c-id="' + comment.id + '" data-direction="1" href="javascript:void;">顶</a> [<span>' + comment.up + '</span>] | ' +
                            '<a class="up-down-btn" data-c-id="' + comment.id + '" data-direction="0" href="javascript:void;">踩</a> [<span>' + comment.down + '</span>] ' +
                            '<a class="go-reply-btn" data-c-id="' + comment.id + '" data-username="' + comment.username + '" href="javascript:void;">回复</a>' +
                            ' 来自' + BlogTool.device[comment.device] + '</span></li>';
                    result += '<ul>';
                    for (var pos in comment.reply) {
                        result += createComment(comment.reply[pos], level);
                    }
                    result += '</ul>'
                    return result;
                }

                function getDescriptionLeaveNow(cTime) {
                    var between = Date.now() / 1000 - cTime / 1000;
                    if (between < 3600) {
                        return (~~(between / 60) + '分钟前');
                    } else if (between < 86400) {
                        return (~~(between / 3600) + '小时前');
                    } else {
                        return (~~(between / 86400) + '天前');
                    }
                }

                function bindEvents(){
                    $('.comment li').on('mouseenter', function () {
                        $(this).addClass('focus-comment');
                        $(this).find('span').removeClass('hidden');
                    }).on('mouseleave', function () {
                        $(this).removeClass('focus-comment');
                        $(this).find('span').addClass('hidden');
                    });

                    $('.up-down-btn').on('click', function () {
                        var $self = $(this);
                        var cId = $self.attr('data-c-id');
                        var direction = $self.attr('data-direction');
                        var data = {cId: cId, direction: direction};
                        $.ajax('/blog/comment/updown', {
                            data: data,
                            success: function (data) {
                                console.log(data.data);
                                var org = Number($self.next('span').text());
                                $self.next('span').text(org + data.data);
                            }, failure: function (data) {
                                console.log(data);
                            }
                        });
                    });
                    $('.reply-content').on('keyup', function () {
                        if ($(this).val() != '') {
                            $('.reply-btn').removeClass('disabled');
                        } else {
                            $('.reply-btn').addClass('disabled');
                        }
                    });

                    $('.go-reply-btn').on('click', function () {

                        $('.reply-content').val('');
                        $('.reply-btn').addClass('disabled');

                        var cId = $(this).attr('data-c-id');
                        $('#reply-c-id').val(cId);
                        var username = $(this).attr('data-username');
                        $('.reply-to').text(username);
                        $('.to-user').removeClass('hidden');
                    });

                    $('.clear-reply-to').on('click', function () {

                        $('.reply-content').val('');
                        $('.reply-btn').addClass('disabled');
                        $('.to-user').addClass('hidden');

                        $('.reply-c-id').val('');
                    });

                    $('.reply-btn').on('click', function () {
                        if (!$(this).hasClass('disabled')) {
                            var content = $('.reply-content').val();
                            var cId = $('.reply-c-id').val();
                            var bId = $('.b-id').val();
                            var device = 0;
                            $.ajax('/blog/comment/reply', {
                                type: 'POST',
                                dataType:'json',
                                data: {
                                    content: content,
                                    cId: cId,
                                    bId: bId,
                                    device: device
                                },
                                success: function (data) {
                                    console.log(data);
                                    if (data.success) {
                                        window.location.reload();
                                    } else {
                                        BlogTool.alert('失败~~~');
                                    }
                                },
                                failure: function (data) {
                                    BlogTool.alert(data.message);
                                }
                            });
                        }
                    });
                }

                var commentJson = ${commentList};
                var commentHtml = '<ul>';
                for (var pos in commentJson) {
                    commentHtml += createComment(commentJson[pos], 0);
                }
                commentHtml += '</ul>';
                $('.comment').html(commentHtml);
                bindEvents();
            }
        });
    </script>
</head>
<body class="container">
<div>
    <a class="btn btn-default" href="/">首页</a>
</div>
<div class="main-panel">
    <h2 class="blog-title">${blog.title}
        <small>${blog.createDate}</small>
    </h2>
    <div class="highlight">${blog.texts}</div>
    <div class="comment">

    </div>
    <div class="reply-container">
        <div class="row">
            <div class="col-xs-offset-2 col-xs-6">
                <div class="to-user hidden">回复 <span class="reply-to"></span>:<a class="clear-reply-to"
                                                                                 href="javascript:void;">x</a></div>
                <textarea class="reply-content" cols="73"></textarea>
            </div>
            <div class="col-xs-1">
                <div>
                    <a class="reply-btn disabled btn btn-xs btn-success">回复</a>
                </div>
            </div>
        </div>
    </div>
    <input type="hidden" class="reply-c-id" value="">
    <input type="hidden" class="b-id" value="${blog.id}">
</div>
<ul class="tool-bar hidden">
    <li id="go-top">
        <a class="go-top-btn"></a>
    </li>
</ul>
<%@include file="../comm/footer.jsp" %>
<script type="text/javascript" charset="utf-8" src="/blog/visit/${blog.id}"></script>
</body>
</html>
