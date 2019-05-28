package com.yl.blog.service;

import com.yl.blog.entity.User;
import com.yl.blog.entity.es.EsBlog;
import com.yl.blog.vo.TagVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/12 10:40
 * @Since 1.0
 */
public interface EsBlogService {
    /**
     * 删除EsBlog
     * @param id
     * @return
     */
    void removeEsBlog(String id);

    /**
     * 更新 EsBlog
     * @param EsBlog
     * @return
     */
    EsBlog updateEsBlog(EsBlog esBlog);

    /**
     * 根据Blog的Id获取EsBlog
     * @param id
     * @return
     */
    EsBlog getEsBlogByBlogId(Long blogId);

    /**
     * 最新博客列表，分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);

    /**
     * 最热博客列表，分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable);

    /**
     * 博客列表，分页
     * @param pageable
     * @return
     */
    Page<EsBlog> listEsBlogs(Pageable pageable);
    /**
     * 最新前5
     * @param keyword
     * @return
     */
    List<EsBlog> listTop5NewestEsBlogs();

    /**
     * 最热前5
     * @param keyword
     * @return
     */
    List<EsBlog> listTop5HotestEsBlogs();

    /**
     * 最热前 30 标签
     * @return
     */
    List<TagVO> listTop30Tags();

    /**
     * 最热前12用户
     * @return
     */
    List<User> listTop12Users();

    public Page<EsBlog> listSimilarEsBlogs(String title, String tages, Pageable pageable);

    List<EsBlog> listSimilarEsBlogs(String title, String tags);

    void UpdateUserAvater(String username);

    void removeEsBlogByUsername(String username);
}
