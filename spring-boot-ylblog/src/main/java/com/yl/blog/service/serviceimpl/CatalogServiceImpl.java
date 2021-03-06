package com.yl.blog.service.serviceimpl;

import com.yl.blog.entity.Catalog;
import com.yl.blog.entity.User;
import com.yl.blog.repository.CatalogRepository;
import com.yl.blog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/11 13:28
 * @Since 1.0
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public Catalog saveCatalog(Catalog catalog) {
        // 判断重复
        List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(),
                catalog.getName());
        if(list !=null && list.size() > 0) {
            throw new IllegalArgumentException("该分类已经存在了");
        }
        return catalogRepository.save(catalog);
    }

    @Override
    public void removeCatalog(Long id) {
        catalogRepository.deleteById(id);
    }

    @Override
    public Optional<Catalog> getCatalogById(Long id) {
        return catalogRepository.findById(id);
    }

    @Override
    public List<Catalog> listCatalogs(User user) {
        return catalogRepository.findByUser(user);
    }
}
