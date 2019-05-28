package com.yl.blog.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @Author: YL丶JUN
 * @Date: 2019/4/30 20:35
 * @Since 1.0
 */
@Entity
@Table(name = "tbl_auth")
public class Authority implements GrantedAuthority {


    private static final long serialVersionUID = 1L;

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id; // 用户的唯一标识

    @Column(nullable = false) // 映射为字段，值不能为空
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.core.GrantedAuthority#getAuthority()
     */
    @Override
    public String getAuthority() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}