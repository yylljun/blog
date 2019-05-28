package com.yl.blog.repository;

import com.yl.blog.entity.Catalog;
import com.yl.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import sun.rmi.runtime.Log;

import java.util.List;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/11 13:29
 * @Since 1.0
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    /**
     * 根据用户查找分类
     * @param user
     * @return
     */
    List<Catalog> findByUser(User user);

    /**
     * 根据用户和分类名查找
     * @param user
     * @param name
     * @return
     */
    List<Catalog> findByUserAndName(User user, String name);

}
