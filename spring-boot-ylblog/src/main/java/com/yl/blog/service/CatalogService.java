package com.yl.blog.service;

import com.yl.blog.entity.Catalog;
import com.yl.blog.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/11 13:28
 * @Since 1.0
 */
public interface CatalogService {
    /**
     * 保存Catalog
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除Catalog
     * @param id
     * @return
     */
    void removeCatalog(Long id);

    /**
     * 根据id获取Catalog
     * @param id
     * @return
     */
    Optional<Catalog> getCatalogById(Long id);

    /**
     * 获取Catalog列表
     * @return
     */
    List<Catalog> listCatalogs(User user);
}
