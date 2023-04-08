package com.mjc.school.repository;

import com.mjc.school.repository.model.NewsModel;

import java.util.List;

public interface ExtraNewsRepository {

    List<NewsModel> readNewsByParams(List<Long> tagId, String tagName, String authorName, String title, String content);
}
