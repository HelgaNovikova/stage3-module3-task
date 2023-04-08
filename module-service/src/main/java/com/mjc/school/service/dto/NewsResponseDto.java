package com.mjc.school.service.dto;

import java.util.List;
import java.util.Objects;

public class NewsResponseDto {
    private long id;
    private String title;
    private String content;
    private String createDate;
    private String lastUpdateDate;
    private long authorId;
    private List<Long> tagIds;

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "NewsDtoResponse[" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", lastUpdatedDate=" + lastUpdateDate +
                ", authorId=" + authorId +
                ", tagIds=" + tagIds.toString() +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsResponseDto that = (NewsResponseDto) o;
        return id == that.id && authorId == that.authorId && Objects.equals(title, that.title) && Objects.equals(content, that.content) && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, createDate, lastUpdateDate, authorId);
    }
}
