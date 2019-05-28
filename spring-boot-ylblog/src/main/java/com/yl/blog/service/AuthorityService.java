package com.yl.blog.service;

import com.yl.blog.entity.Authority;

import java.util.Optional;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/2 13:54
 * @Since 1.0
 */
public interface AuthorityService {
    /**
     * 根据ID查询 Authority
     * @param id
     * @return
     */
    Optional<Authority> getAuthorityById(Long id);
}
