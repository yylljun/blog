package com.yl.blog.service;

import com.yl.blog.entity.Comment;

import java.util.Optional;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/10 19:30
 * @Since 1.0
 */
public interface CommentService {
    /**
     * 根据id获取 Comment
     * @param id
     * @return
     */
    Optional<Comment> getCommentById(Long id);
    /**
     * 删除评论
     * @param id
     * @return
     */
    void removeComment(Long id);
}
