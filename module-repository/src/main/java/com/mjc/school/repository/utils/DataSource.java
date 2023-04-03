package com.mjc.school.repository.utils;

import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;

import java.util.Map;
import java.util.stream.Collectors;

public interface DataSource {
    abstract Map<Long, NewsModel> getNews();

    abstract Map<Long, AuthorModel> getAuthors();
}
