package com.yl.blog.service.serviceimpl;

import com.yl.blog.entity.*;
import com.yl.blog.entity.es.EsBlog;
import com.yl.blog.repository.BlogRepository;
import com.yl.blog.service.BlogService;
import com.yl.blog.service.EsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/4 10:06
import java.util.Optional;

 * @Since 1.0
 */
@Service
public class BlogServiceImpl implements BlogService {


    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsBlogService esBlogService;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew = (blog.getId() == null);
        EsBlog esBlog = null;
        Blog returnBlog = blogRepository.save(blog);

        if (isNew) {
            System.out.println("isNew: New" + blog.getId());
            esBlog = new EsBlog(returnBlog);
        } else {
            esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(returnBlog);
        }

        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.deleteById(id);
        EsBlog esblog = esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esblog.getId());
    }

    @Override
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        String tags = title;
        Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,
                user, tags, user, pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        Blog blogNew = null;

        if (blog.isPresent()) {
            blogNew = blog.get();
            blogNew.setReadSize(blogNew.getReadSize() + 1); // 在原有的阅读量基础上递增1
            this.saveBlog(blogNew);
        }
    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        Blog originalBlog = null;
        if(optionalBlog.isPresent()) {
            originalBlog = optionalBlog.get();
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Comment comment = new Comment(user, commentContent);
            originalBlog.addComment(comment);
        }

        return this.saveBlog(originalBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if(optionalBlog.isPresent()) {
            Blog originalBlog = optionalBlog.get();
            originalBlog.removeComment(commentId);
            this.saveBlog(originalBlog);
        }
    }

    @Override
    public Blog createVote(Long blogId) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        Blog originalBlog = null;

        if (optionalBlog.isPresent()) {
            originalBlog = optionalBlog.get();

            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Vote vote = new Vote(user);
            boolean isExist = originalBlog.addVote(vote);
            if (isExist) {
                throw new IllegalArgumentException("该用户已经点过赞了");
            }
        }

        return this.saveBlog(originalBlog);
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        Blog originalBlog = null;

        if (optionalBlog.isPresent()) {
            originalBlog = optionalBlog.get();
            originalBlog.removeVote(voteId);
            this.saveBlog(originalBlog);
        }
    }

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByCatalog(catalog, pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitle(String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        Page<Blog> blogs = blogRepository.findByTitleLike(title, pageable);
        return blogs;
    }

    @Transactional
    @Override
    public void removeBlogByUser(User user) {
        blogRepository.deleteBlogsByUser(user);
        esBlogService.removeEsBlogByUsername(user.getUsername());
    }


}
