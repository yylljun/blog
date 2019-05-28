/*!
  * Bolg main JS.
 *
 * @since: 1.0.0 2017/3/9
 * @author Way Lau <https://waylau.com>
 */
"use strict";
//# sourceURL=main.js

// DOM 加载完再执行
$(function() {

    var _pageSize; // 存储用于搜索
    var fileId;

    // 根据用文件、页面索引、页面大小获取文件列表
    function getFilesByName(pageIndex, pageSize) {
        $.ajax({
            url: "/files",
            contentType : 'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "name":$("#searchName").val()
            },
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    }

    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getFilesByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 搜索
    $("#searchNameBtn").unbind('click').click(function() {
        getFilesByName(0, _pageSize);
    });



    // 删除文件
    $("#rightContainer").on("click",".blog-delete-file", function () {
        // 获取 CSRF Token
        fileId = $(this).attr("fileId");
    });

    $("#uploadFile").unbind('click').click(function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var formData = new FormData();
        formData.append("file", document.getElementById("file1").files[0]);
        $.ajax({
            url: "/files",
            contentType : false,
            data: formData,
            type: 'POST',
            processData: false,
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function(data){
                if (data.success) {
                    // 从新刷新主界面
                    getFilesByName(0, _pageSize);
                    toastr.success(data.message);

                } else {
                    toastr.error(data.message);
                }
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });


    $("#submitDelFile").unbind('click').click(function () {
            // 获取 CSRF Token
            var csrfToken = $("meta[name='_csrf']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");
            $.ajax({
                url: "/files/" + fileId,
                type: 'DELETE',
                beforeSend: function(request) {
                    request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
                },
                success: function(data){
                    if (data.success) {
                        // 从新刷新主界面
                        getFilesByName(0, _pageSize);
                        toastr.success(data.message);
                    } else {
                        toastr.error(data.message);
                    }
                },
                error : function() {
                    toastr.error("error!");
                }
            });
        });

});