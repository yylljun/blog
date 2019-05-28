package com.yl.blog.controller;

import com.yl.blog.entity.Blog;
import com.yl.blog.entity.File;
import com.yl.blog.entity.User;
import com.yl.blog.entity.es.EsBlog;
import com.yl.blog.service.BlogService;
import com.yl.blog.service.EsBlogService;
import com.yl.blog.vo.Response;
import com.yl.blog.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 主页控制器.
 * 
 * @since 1.0.0 2017年3月8日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {
	@Autowired
	private EsBlogService esBlogService;

	@Autowired
	private BlogService blogService;

	@GetMapping
	public String listEsBlogs(
			@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
			@RequestParam(value="async",required=false) boolean async,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			Model model) {

		Page<EsBlog> page = null;
		List<EsBlog> list = null;
		boolean isEmpty = true; // 系统初始化时，没有博客数据
		try {
			if (order.equals("hot")) { // 最热查询
				Sort sort = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
				Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
				page = esBlogService.listHotestEsBlogs(keyword, pageable);
			} else if (order.equals("new")) { // 最新查询
				Sort sort = new Sort(Sort.Direction.DESC,"createTime");
				Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
				page = esBlogService.listNewestEsBlogs(keyword, pageable);
			}

			isEmpty = false;
		} catch (Exception e) {
			System.out.println("异常");
			Pageable pageable = PageRequest.of(0, 10);
			page = esBlogService.listEsBlogs(pageable);
		}

		list = page.getContent();   // 当前所在页面数据列表


		model.addAttribute("order", order);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("blogList", list);

		// 首次访问页面才加载
		if (!async && !isEmpty) {
			List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
			model.addAttribute("newest", newest);
			List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
			model.addAttribute("hotest", hotest);
			List<TagVO> tags = esBlogService.listTop30Tags();
			model.addAttribute("tags", tags);
			List<User> users = esBlogService.listTop12Users();
			model.addAttribute("users", users);
		}

		return (async==true?"/index :: #mainContainerRepleace":"/index");
	}

	/**
	 * 管理员删除博客
	 * @param username
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<Response> adminDeleteBlog(@PathVariable("id") Long id) {

		try {
			blogService.removeBlog(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}


	@GetMapping("/admins")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ModelAndView list(@RequestParam(value = "async", required = false) boolean async,
							 @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
							 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
							 @RequestParam(value = "title", required = false, defaultValue = "") String title, Model model) {
		Sort sort = new Sort(Sort.Direction.DESC,"createTime");
		Pageable pageable = PageRequest.of(pageIndex, pageSize,sort);
		System.out.println("Blog搜索请求" + title);
		Page<Blog> page = blogService.listBlogsByTitle(title, pageable);
		List<Blog> list = page.getContent(); // 当前所在页面数据列表
		model.addAttribute("page", page);
		model.addAttribute("blogList", list);
		return new ModelAndView(async == true ? "blogs/list :: #mainContainerRepleace" : "blogs/list", "blogModel",
				model);
	}

}
