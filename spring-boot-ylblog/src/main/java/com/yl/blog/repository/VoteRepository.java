package com.yl.blog.repository;

import com.yl.blog.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/10 21:34
 * @Since 1.0
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
