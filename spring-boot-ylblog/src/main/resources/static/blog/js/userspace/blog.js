/*!
 * blog.html 页面脚本.
 * 
 * @since: 1.0.0 2017-03-26
 * @author Way Lau <https://waylau.com>
 */
"use strict";
//# sourceURL=blog.js

// DOM 加载完再执行
$(function() {
	$.catalog("#catalog", ".post-content");

	// 处理删除博客事件

	$(".blog-content-container").on("click",".blog-delete-blog", function () {
		// 获取 CSRF Token 
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");


		$.ajax({
			url: $(this).attr("blogUrl") ,
			type: 'DELETE',
			beforeSend: function(request) {
				request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
			},
			success: function(data){
				if (data.success) {
					// 成功后，重定向
					window.location = data.body;
				} else {
					toastr.error(data.message);
				}
			},
			error : function() {
				toastr.error("error!");
			}
		});
	});

	// 获取评论列表
	function getCommnet(blogId) {
		// 获取 CSRF Token
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");

		$.ajax({
			url: '/comments',
			type: 'GET',
			data:{"blogId":blogId},
			beforeSend: function(request) {
				request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
			},
			success: function(data){
				$("#mainContainer").html(data);

			},
			error : function() {
				toastr.error("error!");
			}
		});
	}

	// 提交评论
	$(".blog-content-container").on("click","#submitComment", function () {
		// 获取 CSRF Token
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");

		$.ajax({
			url: '/comments',
			type: 'POST',
			data:{"blogId":blogId, "commentContent":$('#commentContent').val()},
			beforeSend: function(request) {
				request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
			},
			success: function(data){
				if (data.success) {
					// 清空评论框
					$('#commentContent').val('');
					// 获取评论列表
					getCommnet(blogId);
				} else {
					toastr.error(data.message);
				}
			},
			error : function() {
				toastr.error("error!");
			}
		});
	});

	$(".blog-content-container").on("click",".blog-delete-comment", function () {
		// 获取 CSRF Token
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");

		$.ajax({
			url: '/comments/'+$(this).attr("commentId")+'?blogId='+blogId,
			type: 'DELETE',
			beforeSend: function(request) {
				request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
			},
			success: function(data){
				if (data.success) {
					// 获取评论列表
					getCommnet(blogId);
				} else {
					toastr.error(data.message);
				}
			},
			error : function() {
				toastr.error("error!");
			}
		});
	});

	// 提交点赞
	$(".blog-content-container").on("click","#submitVote", function () {
		// 获取 CSRF Token
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
			url: '/votes',
			type: 'POST',
			data:{"blogId":blogId},
			beforeSend: function(request) {
				request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
			},
			success: function(data){
				if (data.success) {

					$("#submitVote").attr("hidden", "hidden");
					$("#cancelVote").removeAttr("hidden");
					$("#cancelVote").attr("voteId", data.body.id);
					var $voteSize = $(".card-text .fa-thumbs-o-up");
					$voteSize.text(Number($voteSize.text()) + 1);
					toastr.info(data.message);
					// 成功后，重定向
				} else {
					toastr.error(data.message);
				}
			},
			error : function() {
				toastr.error("error!");
			}
		});
	});

	// 取消点赞
	$(".blog-content-container").on("click","#cancelVote", function () {
		// 获取 CSRF Token
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
			url: '/votes/'+$(this).attr('voteId')+'?blogId='+blogId,
			type: 'DELETE',
			beforeSend: function(request) {
				request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
			},
			success: function(data){
				if (data.success) {
					$("#cancelVote").attr("hidden", "hidden");
					$("#submitVote").removeAttr("hidden");
					//点赞量表面-1
					var $voteSize = $(".card-text .fa-thumbs-o-up");
					$voteSize.text(Number($voteSize.text()) - 1);
					toastr.info(data.message);
				} else {
					toastr.error(data.message);
				}
			},
			error : function() {
				toastr.error("error!");
			}
		});
	});

	// 初始化 博客评论
	getCommnet(blogId);

});