package com.mjc.school.service.dto;

import java.util.List;

public class NewsCreateDto {
    Long authorId;
    String title;
    String content;
    Long id;
    List<Long> tagIds;

    public NewsCreateDto() {
    }

    public NewsCreateDto(Long id, String title, String content, Long authorId) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.id = id;
    }

    public NewsCreateDto(Long id, String title, String content, Long authorId, List<Long> tagIds) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.id = id;
        this.tagIds = tagIds;
    }

    public NewsCreateDto(String title, String content, Long authorId) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
    }

    public NewsCreateDto(String title, String content, Long authorId, List<Long> tagIds) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.tagIds = tagIds;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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
}
