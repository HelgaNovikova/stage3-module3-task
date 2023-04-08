package com.mjc.school.repository.utils;

import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.impl.TagRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class DataBaseLoader implements InitializingBean {

    public static final int DEFAULT_NEWS_COUNT_TO_GENERATE = 20;

    private final AuthorRepository authorRepo;
    private final TagRepository tagRepo;
    private final NewsRepository newsRepo;

    @Autowired
    public DataBaseLoader(AuthorRepository authorRepo, TagRepository tagRepo, NewsRepository newsRepo) {
        this.authorRepo = authorRepo;
        this.tagRepo = tagRepo;
        this.newsRepo = newsRepo;
    }

    private List<AuthorModel> writeAuthorsFromFileToDB(String path) {
        List<String> lines = readFromFile(path);
        long id = 1;
        List<AuthorModel> authorsFromFile = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (String line : lines) {
            AuthorModel authorModel = new AuthorModel(id, line, now, now);
            authorsFromFile.add(authorModel);
            id++;
            authorRepo.saveAuthorToDB(authorModel);
        }
        return authorsFromFile;
    }

    private List<TagModel> writeTagsFromFileToDB(String path) {
        List<String> lines = readFromFile(path);
        long id = 1;
        List<TagModel> tagsFromFile = new ArrayList<>();
        for (String line : lines) {
            TagModel tagModel = new TagModel(id, line);
            tagsFromFile.add(tagModel);
            id++;
            tagRepo.saveTagToDB(tagModel);
        }
        return tagsFromFile;
    }

    private List<String> readTitlesFromFiles(String titlePath) {
        return readFromFile(titlePath);
    }

    private List<String> readContentsFromFile(String contentPath) {
        return readFromFile(contentPath);
    }

    private List<String> readFromFile(String contentPath) {
        try {
            URL resource = Objects.requireNonNull(NewsRepository.class.getClassLoader().getResource(contentPath));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
                return new ArrayList<>(reader.lines().toList());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file " + contentPath, e);
        }
    }

    private void loadNewsToDB(String titlesPath, String authorsPath, String contentPath, String tagPath) {
        List<String> titlesFromFile = readTitlesFromFiles(titlesPath);
        List<AuthorModel> authorsFromFile = writeAuthorsFromFileToDB(authorsPath);
        List<String> contentFromFile = readContentsFromFile(contentPath);
        List<TagModel> tagsFromFile = writeTagsFromFileToDB(tagPath);
        for (int i = 0; i < DEFAULT_NEWS_COUNT_TO_GENERATE; i++) {
            LocalDateTime now = LocalDateTime.now();
            if (contentFromFile.get(i).length() < 255 && titlesFromFile.get(i).length() < 255) {
                NewsModel news = new NewsModel((long) i, titlesFromFile.get(i), contentFromFile.get(i), now, now, authorsFromFile.get(i));
                newsRepo.saveNewsToDB(news);
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        loadNewsToDB("news", "authors", "content", "tag");
    }
}
