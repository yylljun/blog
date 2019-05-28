/*!
 * blogedit.html 页面脚本.
 * 
 * @since: 1.0.0 2017-03-26
 * @author Way Lau <https://waylau.com>
 */
"use strict";
//# sourceURL=blogedit.js

// DOM 加载完再执行
$(function () {

    $('.form-control-chosen').chosen();

    // 发布博客
    $("#submitBlog").click(function () {

        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        console.log("$('.tag').val()" + $('.tag').val());
        $.ajax({
            url: '/u/' + $(this).attr("userName") + '/blogs/edit',
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                "id": $('#blogId').val(),
                "title": $('#title').val(),
                "summary": $('#summary').val(),
                "content": $('#content').val(),
                "htmlContent": $('#htmlContent').val(),
                "catalog": {"id": $('#catalogSelect').val()},
                "tags": $('#blog_tags').val()
            }),
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    // 成功后，重定向
                    window.location = data.body;
                } else {
                    toastr.error("error!" + data.message);
                }

            },
            error: function () {
                toastr.error("error!");
            }
        })
    })

    //获取新增分类的页面
    $(".blog-content-container").on("click", ".blog-add-catalog", function () {
        $.ajax({
            url: '/catalogs/edit',
            type: 'GET',
            success: function (data) {
                if (data.success != null) {
                    $("#catalogFormContainer").html(data.message);
                } else {
                    $("#catalogFormContainer").html(data);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });
    // 提交分类
    $("#submitEditCatalog").click(function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url: '/catalogs',
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                "username": username,
                "catalog": {"id": $('#catalogId').val(), "name": $('#catalogName').val()}
            }),
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    toastr.info(data.message);
                    // 成功后，刷新列表
                    getCatalogsSelect(username);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    // 刷新分类下拉选择框方法
    // 获取分类列表
    function getCatalogsSelect(username) {
        // 获取 CSRF Token

        $.ajax({
            url: '/catalogs',
            type: 'GET',
            data: {
                "username": username,
                "isblogedit": true
            },
            success: function (data) {
                $("#catalogSelectContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }

    getCatalogsSelect(username)
})