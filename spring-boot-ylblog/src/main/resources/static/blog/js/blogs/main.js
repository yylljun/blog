/**
 * Bolg main JS.
 * Created by waylau.com on 2017/3/9.
 */
"use strict";
//# sourceURL=main.js

// DOM 加载完再执行
$(function() {

	var _pageSize; // 存储用于搜索
	var blogId;

	// 根据用文件、页面索引、页面大小获取文件列表
	function getBlogsByTitle(pageIndex, pageSize) {
		$.ajax({
			url: "/blogs/admins",
			contentType : 'application/json',
			data:{
				"async":true,
				"pageIndex":pageIndex,
				"pageSize":pageSize,
				"title":$("#searchName").val()
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
		getBlogsByTitle(pageIndex, pageSize);
		_pageSize = pageSize;
	});

	// 搜索
	$("#searchNameBtn").unbind('click').click(function() {
		getBlogsByTitle(0, _pageSize);
	});



	// 删除文件
	$("#rightContainer").on("click",".blog-delete-blog", function () {
		// 获取 CSRF Token
		blogId = $(this).attr("blogId");
	});


	$("#submitDelBlog").unbind('click').click(function () {
		// 获取 CSRF Token
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
		// /{username}/blogs/{id}
			url: "/blogs/" + blogId,
			type: 'DELETE',
			beforeSend: function(request) {
				request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
			},
			success: function(data){
				if (data.success) {
					// 从新刷新主界面
					getBlogsByTitle(0, _pageSize);
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