package com.yl.blog.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: YL丶JUN
 * @Date: 2019/5/4 10:02
 * @Since 1.0
 */
@Entity // 实体
public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id; // 用户的唯一标识

    @NotEmpty(message = "标题不能为空")
    @Size(min=2, max=50)
    @Column(nullable = false, length = 50) // 映射为字段，值不能为空
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @Size(min=2, max=300)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String summary;

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content;

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String htmlContent; // 将 md 转为 html



    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Timestamp createTime;
    @Column(name="readSize")
    private Integer readSize = 0; // 访问量、阅读量

    @Column(name="commentSize")
    private Integer commentSize = 0;  // 评论量

    @Column(name="likeSize")
    private Integer likeSize = 0;  // 点赞量

    @Column(name="voteSize")
    private Integer voteSize = 0;  // 点赞量


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)  //联级操作ALL(MERGE,PERSIST,REFERESH,REMOVE)(更新，保存，刷新，删除)
    //一对多，多对多时使用@JoinTable（申明关联表）   持有关联数据的一方称为owning-side（Blog），另一方称为inverse-side(Comment)。该方为owning-side
    //关联表明为blog_comment ，关联表列 blog_id comment_id       这里name是本类（表）在数据库的字段名，referencedColumnName 是关联类（表）在数据库中的关联字段名
    @JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "blog_vote", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id", referencedColumnName = "id"))
    private List<Vote> votes;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="catalog_id")
    private Catalog catalog;

    @Column(name="tags", length = 100)
    private String tags;  // 标签

    protected Blog() {
        // TODO Auto-generated constructor stub
    }
    public Blog(String title, String summary,String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getReadSize() {
        return readSize;
    }

    public void setReadSize(Integer readSize) {
        this.readSize = readSize;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public Integer getVoteSize() {
        return voteSize;
    }

    public void setVoteSize(Integer voteSize) {
        this.voteSize = voteSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public Integer getLikeSize() {
        return likeSize;
    }

    public void setLikeSize(Integer likeSize) {
        this.likeSize = likeSize;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentSize = this.comments.size();
    }

    /**
     * 添加评论
     * @param comment
     */
    public void addComment(Comment comment) {
        this.comments.add(comment);
        this.commentSize = this.comments.size();
    }
    /**
     * 删除评论
     * @param comment
     */
    public void removeComment(Long commentId) {
        for (int index=0; index < this.comments.size(); index ++ ) {
            if (comments.get(index).getId() == commentId) {
                this.comments.remove(index);
                break;
            }
        }

        this.commentSize = this.comments.size();
    }

    /**
     * 点赞
     * @param vote
     * @return
     */
    public boolean addVote(Vote vote) {
        boolean isExist = false;
        // 判断重复
        for (int index=0; index < this.votes.size(); index ++ ) {
            if (this.votes.get(index).getUser().getId() == vote.getUser().getId()) {
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            this.votes.add(vote);
            this.voteSize = this.votes.size();
        }

        return isExist;
    }
    /**
     * 取消点赞
     * @param voteId
     */
    public void removeVote(Long voteId) {
        for (int index=0; index < this.votes.size(); index ++ ) {
            if (this.votes.get(index).getId() == voteId) {
                this.votes.remove(index);
                break;
            }
        }

        this.voteSize = this.votes.size();
    }
    public List<Vote> getVotes() {
        return votes;
    }
    public void setVotes(List<Vote> votes) {
        this.votes = votes;
        this.voteSize = this.votes.size();
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", htmlContent='" + htmlContent + '\'' +
                ", user=" + user +
                ", createTime=" + createTime +
                ", readSize=" + readSize +
                ", commentSize=" + commentSize +
                ", likeSize=" + likeSize +
                ", voteSize=" + voteSize +
                ", comments=" + comments +
                ", votes=" + votes +
                ", catalog=" + catalog +
                ", tags='" + tags + '\'' +
                '}';
    }
}