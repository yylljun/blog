package com.yl.blog.repository;

import com.yl.blog.entity.Blog;
import com.yl.blog.entity.Catalog;
import com.yl.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/4 10:04
 * @Since 1.0
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {
    /**
     * 根据用户名、博客标题分页查询博客列表
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

    Page<Blog> findByTitleLike(String title, Pageable pageable);
    /**
     * 根据用户名、博客查询博客列表（时间逆序）
     * @param title
     * @param user
     * @param tags
     * @param user2
     * @param pageable
     * @return
     */
    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title,
                                                                            User user, String tags, User user2, Pageable pageable);

    /**
     * 根据分类查询博客列表
     * @param catalog
     * @param pageable
     * @return
     */
    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);

    void deleteBlogsByUser(User user);

}