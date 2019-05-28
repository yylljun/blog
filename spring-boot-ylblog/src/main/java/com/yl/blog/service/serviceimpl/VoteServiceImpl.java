package com.yl.blog.service.serviceimpl;

import com.yl.blog.entity.Vote;
import com.yl.blog.repository.VoteRepository;
import com.yl.blog.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/10 21:41
 * @Since 1.0
 */
@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Optional<Vote> getVoteById(Long id) {
        return voteRepository.findById(id);
    }

    @Override
    public void removeVote(Long id) {
        voteRepository.deleteById(id);
    }
}
