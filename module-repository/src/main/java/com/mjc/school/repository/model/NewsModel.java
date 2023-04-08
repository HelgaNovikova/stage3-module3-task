package com.mjc.school.repository.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "News")
public class NewsModel implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private LocalDateTime createDate;

    @Column
    private LocalDateTime lastUpdateDate;

    @ManyToOne
    @JoinColumn(name = "authorId")
    private AuthorModel author;

    @ManyToMany//(fetch = FetchType.EAGER)
    @JoinTable(name = "news_tags",
            joinColumns = @JoinColumn(name = "tagId"),
            inverseJoinColumns = @JoinColumn(name = "newsId"))
    private List<TagModel> newsTags;

    public List<TagModel> getNewsTags() {
        return newsTags;
    }

    public void setNewsTags(List<TagModel> newsTags) {
        this.newsTags = newsTags;
    }

    public NewsModel() {
    }

    public NewsModel(Long id, String title, String content, LocalDateTime createDate, LocalDateTime lastUpdateDate, AuthorModel author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.author = author;
        this.newsTags = null;
    }

    public NewsModel(Long id, String title, String content, LocalDateTime createDate, LocalDateTime lastUpdateDate, AuthorModel author, List<TagModel> newsTags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.author = author;
        this.newsTags = newsTags;
    }

//    public List<Long> getTagIds() {
//        List<Long> tagIds = new ArrayList<>();
//        for (TagModel tag: this.newsTags){
//            long id = tag.getId();
//            tagIds.add(id);
//        }
//        return tagIds;
//    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public AuthorModel getAuthor() {
        return author;
    }

    public void setAuthor(AuthorModel author) {
        this.author = author;
    }
}
