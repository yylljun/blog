package com.yl.blog.repository;

import com.yl.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/10 19:29
 * @Since 1.0
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
