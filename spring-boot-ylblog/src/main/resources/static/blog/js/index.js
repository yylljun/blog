/*!
 * index main JS.
 *
 * @since: 1.0.0 2017/4/12
 * @author Way Lau <https://waylau.com>
 */
"use strict";
//# sourceURL=index.js

// DOM 加载完再执行
$(function() {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getBlogsByName(pageIndex, pageSize) {
        console.log("getBlogsByName");
        $.ajax({
            url: "/blogs",
            contentType : 'application/json',
            data:{
                "async":true,
                "order": $("#headerNav .active").data("order"),
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "keyword":$("#indexkeyword").val()
            },
            success: function(data){
                $("#mainContainer").html(data);

                var keyword = $("#indexkeyword").val();

                // 如果是分类查询，则取消最新、最热选中样式
                if (keyword.length > 0) {
                    $("#headerNav .nav-link").removeClass("active");
                }
            },
            error : function() {
                toastr.error("error!");
            }
        });
    }

    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getBlogsByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 关键字搜索
    $("#indexsearch").click(function() {
        if (window.location.pathname.indexOf("/blogs") !== 0) {//非首页请求，跳转到首页
            window.location.href = '/blogs/?keyword=' + $("#indexkeyword").val();
        }else {
        getBlogsByName(0, _pageSize);
        }
    });

    // 最新\最热切换事件
    $("#headerNav .nav-link").click(function() {

        var url = $(this).attr("url");

        // 先移除其他的点击样式，再添加当前的点击样式
        $("#headerNav .nav-link").removeClass("active");
        $(this).addClass("active");
        if (window.location.pathname.indexOf("/blogs") !== 0) {//非首页请求，跳转到首页
            window.location.href = url;
        }else {
            // 加载其他模块的页面到右侧工作区
            getBlogsByName(0, _pageSize);
        }

        // 清空搜索框内容
        $("#indexkeyword").val('');
    });

    //退出登陆
    $("#logout").click(function () {
        $.ajax({
            url: "/logout",
            type: "POST",
            beforeSend: function (request) {
                // 获取 CSRF Token
                var csrfToken = $("meta[name='_csrf']").attr("content");
                var csrfHeader = $("meta[name='_csrf_header']").attr("content");
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function () {
                window.location.reload();
            }
        })
    });

    toastr.options = {
        "closeButton": true, //是否显示关闭按钮
        "debug": false, //是否使用debug模式
        "positionClass": "toast-bottom-right",//弹出窗的位置
        "showDuration": "300",//显示的动画时间
        "hideDuration": "300",//消失的动画时间
        "timeOut": "4000", //展现时间
        "extendedTimeOut": "1000",//加长展示时间
        "showEasing": "swing",//显示时的动画缓冲方式
        "hideEasing": "linear",//消失时的动画缓冲方式
        "showMethod": "fadeIn",//显示时的动画方式
        "hideMethod": "fadeOut" //消失时的动画方式
    };

});