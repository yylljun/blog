package com.yl.blog.vo;

import com.yl.blog.entity.Catalog;

import java.io.Serializable;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/11 13:38
 * @Since 1.0
 */
public class CatalogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private Catalog catalog;

    public CatalogVO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
