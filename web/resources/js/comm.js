/**
 *
 * Created by Administrator on 2017/5/9.
 */


(function ($) {
    if (!window.BlogTool) {
        window['BlogTool'] = {}
    }
    /**
     *检查ajax请求时,是否已登录
     *
     * @param data
     */
    function checkLogin(data) {
        if(data.status===404){
            alert1("请求地址无效,");
            return false;
        }
        var contentType = data.headers.map["Content-Type"][0];
        if (contentType && contentType.indexOf("html") != -1) {
            alert1("登录超时,或没有权限.请重新登录");
            alertLogonDialog();
            return false;
        }
        return true;
    }

    function alertLogonDialog() {
        var $logonPanel = $('#logon-panel');
        var $maskDiv = $('.logon-mask');
        var $body = $('body');
        $maskDiv.appendTo($body);
        $body.addClass('no-scroll');
        $maskDiv.removeClass('hidden');

        $('.cancel-logon-btn').one('click', function () {
            $maskDiv.addClass('hidden');
            $body.removeClass('no-scroll');
        });
        $('.ajax-logon-btn').one('click', function () {
            $.ajax($logonPanel.attr('data-action'), {
                data: {
                    username: $logonPanel.find('#username').val(),
                    password: $logonPanel.find('#password').val()
                },
                type:'POST',
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $maskDiv.addClass('hidden');
                        $body.removeClass('no-scroll');

                    } else {
                        alert(data.message);
                    }
                }
            });
        });
    }

    window['BlogTool']['checkLogin'] = checkLogin;

    function goUrl(url) {
        url = url || "/";
        window.location.href = url;
    }

    function alert1(msg, opt) {
        alert(msg);
    }

    window['BlogTool']['alert'] = alert1;

    function setCookie(name, value, days) {
        var d = new Date;
        d.setTime(d.getTime() + 24 * 60 * 60 * 1000 * days);
        window.document.cookie = name + '=' + value + ';path=/;expires=' + d.toGMTString();
    }

    window['BlogTool']['setCookie'] = setCookie;

    function getCookie(name) {
        var v = window.document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
        return v ? v[2] : null;
    }

    window['BlogTool']['getCookie'] = getCookie;

    function deleteCookie(name) {
        setCookie(name, '', -1);
    }

    window['BlogTool']['deleteCookie'] = deleteCookie;

    function sendMessage(username) {
        alert1('向' + username + '发消息!');
    }

    window['BlogTool']['sendMessage'] = sendMessage;

})($);
