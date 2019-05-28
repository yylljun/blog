package com.yl.blog.repository;


import com.yl.blog.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * File 存储库.
 * 
 * @since 1.0.0 2017年3月28日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
public interface FileRepository extends MongoRepository<File, String> {
    File findFirstByMd5(String md5);

    /**
     * 根据用户名分页查询用户列表
     * @param name
     * @param pageable
     * @return
     */
    public Page<File> findByNameLike(String name, Pageable pageable);
}
