/**
 * Bolg main JS.
 * Created by waylau.com on 2017/3/9.
 */
"use strict";
//# sourceURL=main.js

// DOM 加载完再执行
$(function() {

	// 菜单事件
	$(".blog-menu .list-group-item").click(function() {

		var url = $(this).attr("url");

		// 先移除其他的点击样式，再添加当前的点击样式
		$(".blog-menu .list-group-item").removeClass("active");
		$(this).addClass("active");

		// 加载其他模块的页面到右侧工作区
		$.ajax({
			url: url,
			success: function(data){
				$("#rightContainer").html(data);
			},
			error : function() {
				alert("error");
			}
		});
	});


	// 选中菜单第一项
	$(".blog-menu .list-group-item:first").trigger("click");




	var _pageSize; // 存储用于搜索
	var blogId;
	var delname;

	// 根据用文件、页面索引、页面大小获取文件列表
	function getBlogsByNameOrTitle(pageIndex, pageSize) {
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
		getBlogsByNameOrTitle(pageIndex, pageSize);
		_pageSize = pageSize;
	});

	// 搜索
	$("#searchNameBtn").unbind('click').click(function() {
		getBlogsByNameOrTitle(0, _pageSize);
	});



	// 删除文件
	$("#rightContainer").on("click",".blog-delete-blog", function () {
		// 获取 CSRF Token
		blogId = $(this).attr("blogId");
		delname = $(this).attr("delname");
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
					getBlogsByNameOrTitle(0, _pageSize);
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