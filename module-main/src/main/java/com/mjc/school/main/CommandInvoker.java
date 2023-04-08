package com.mjc.school.main;

import com.mjc.school.controller.command.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class CommandInvoker {

    private final CommandExecutor executor;

    @Autowired
    public CommandInvoker(CommandExecutor executor) {
        this.executor = executor;
    }

    public Object readAllNews() {
        return new CommandImpl("news.readAll", executor).execute();
    }

    public Object readNewsById(Long id) {
        return new CommandImpl("news.readById", null, Map.of("id", id), executor).execute();
    }

    public Object createNews(String title, String content, String authorId, String tagIds) {
        var body = Map.of("title", title,
                "content", content, "authorId", authorId, "tagIds", tagIds);
        return new CommandImpl("news.create", body, null, executor).execute();
    }

    public Object updateNews(String id, String title, String content, String authorId, String tagIds) {
        var body = Map.of("id", id, "title", title,
                "content", content, "authorId", authorId, "tagIds", tagIds);
        return new CommandImpl("news.update", body, null, executor).execute();
    }

    public boolean deleteNewsById(Long id) {
        return new CommandImpl("news.deleteById", null, Map.of("id", id), executor).execute();
    }

    public Object readAllAuthors() {
        return new CommandImpl("authors.readAll", executor).execute();
    }

    public Object readAuthorsById(Long id) {
        return new CommandImpl("authors.readById", null, Map.of("id", id), executor).execute();
    }

    public Object createAuthors(String name) {
        return new CommandImpl("authors.create", Map.of("name", name), null, executor).execute();
    }

    public Object updateAuthors(String id, String name) {
        return new CommandImpl("authors.update", Map.of("id", id, "name", name), null, executor).execute();
    }

    public boolean deleteAuthorsById(Long id) {
        return new CommandImpl("authors.deleteById", null, Map.of("id", id), executor).execute();
    }

    public Object readAllTags() {
        return new CommandImpl("tags.readAll", executor).execute();
    }

    public Object readTagById(Long id) {
        return new CommandImpl("tags.readById", null, Map.of("id", id), executor).execute();
    }

    public Object createTag(String name) {
        return new CommandImpl("tags.create", Map.of("name", name), null, executor).execute();
    }

    public Object updateTag(String id, String name) {
        return new CommandImpl("tags.update", Map.of("id", id, "name", name), null, executor).execute();
    }

    public boolean deleteTagById(Long id) {
        return new CommandImpl("tags.deleteById", null, Map.of("id", id), executor).execute();
    }

    public Object readAuthorByNewsId(Long newsId) {
        return new CommandImpl("news.readAuthorByNewsId", null, Map.of("id", newsId), executor).execute();
    }

    public Object readTagsByNewsId(Long newsId) {
        return new CommandImpl("news.readTagsByNewsId", null, Map.of("id", newsId), executor).execute();
    }

    public Object readNewsByParams(String tagIds, String tagName, String authorName, String title, String content) {
        List<Long> tags = Arrays.stream(tagIds.split(",")).map(Long::valueOf).toList();
        return new CommandImpl("news.readNewsByParams", null,
                Map.of("tagId", tags, "tagName", tagName, "authorName", authorName, "title", title, "content", content), executor)
                .execute();
    }
}
