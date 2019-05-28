package com.yl.blog.service;

import com.yl.blog.entity.Vote;

import java.util.Optional;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/10 21:37
 * @Since 1.0
 */
public interface VoteService {
    /**
     * 根据id获取 Vote
     * @param id
     * @return
     */
    Optional<Vote> getVoteById(Long id);
    /**
     * 删除Vote
     * @param id
     * @return
     */
    void removeVote(Long id);
}