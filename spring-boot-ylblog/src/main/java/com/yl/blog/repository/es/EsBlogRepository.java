package com.yl.blog.repository.es;

import com.yl.blog.entity.es.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


/**
 * EsBlog Repository 接口
 *
 * @Author: YL丶JUN
 * @Date: 2019/4/27 21:14
 * @Since 1.0
 */
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {
    /**
     * 模糊查询(去重)
     *
     * @param title
     * @param Summary
     * @param content
     * @param tags
     * @param pageable
     * @return
     */
    Page<EsBlog> findByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(
            String title, String summary, String content, String tags, Pageable pageable);

    Page<EsBlog> findByTitleLikeOrSummaryLikeOrContentLikeOrTagsLike(
            String title, String summary, String content, String tags, Pageable pageable);

    Page<EsBlog> findByTitleLikeOrTagsContaining(String title, String tags, Pageable pageable);

    /**
     * 根据 Blog 的id 查询 EsBlog
     *
     * @param blogId
     * @return
     */
    EsBlog findByBlogId(Long blogId);

    List<EsBlog> queryEsBlogByUsername(String username);

    void deleteEsBlogsByUsername(String username);

    void deleteAllByUsername(String username);

}
