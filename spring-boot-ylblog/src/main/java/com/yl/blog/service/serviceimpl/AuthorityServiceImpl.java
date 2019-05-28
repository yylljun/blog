package com.yl.blog.service.serviceimpl;

import com.yl.blog.entity.Authority;
import com.yl.blog.repository.AuthorityRepository;
import com.yl.blog.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/2 13:54
 * @Since 1.0
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Optional<Authority> getAuthorityById(Long id) {
        return authorityRepository.findById(id);
    }
}
