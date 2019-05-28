package com.yl.blog.service.serviceimpl;

import com.yl.blog.entity.Comment;
import com.yl.blog.repository.CommentRepository;
import com.yl.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/10 19:31
 * @Since 1.0
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public void removeComment(Long id) {
        commentRepository.deleteById(id);
    }}
