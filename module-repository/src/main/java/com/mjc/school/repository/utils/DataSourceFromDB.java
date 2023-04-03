package com.mjc.school.repository.utils;

import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataSourceFromDB implements InitializingBean, DataSource {

    public static final int DEFAULT_NEWS_COUNT_TO_GENERATE = 20;
    public static final String SELECT_TAG_BY_ID = "select t from TagModel t where id = :id";
    private final Map<Long, NewsModel> news = new HashMap<>();
    private final Map<Long, TagModel> tags = new HashMap<>();
    private final Map<Long, AuthorModel> authors = new HashMap<>();
    EntityManager entityManager;
    private TransactionTemplate transactionTemplate;

    @Autowired
    public DataSourceFromDB(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
    }

    private List<AuthorModel> writeAuthorsFromFileToDB(String path) {
        return transactionTemplate.execute(s -> {
            List<String> lines = readFromFile(path);
            long id = 1;
            List<AuthorModel> authorsFromFile = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for (String line : lines) {
                AuthorModel authorModel = new AuthorModel(id, line, now, now);
                authorsFromFile.add(authorModel);
                id++;
                entityManager.merge(authorModel);
            }
            return authorsFromFile;
          });
    }

    private List<TagModel> writeTagsFromFileToDB(String path) {
        return transactionTemplate.execute(s -> {
            List<String> lines = readFromFile(path);
            long id = 1;
            List<TagModel> tagsFromFile = new ArrayList<>();
            for (String line : lines) {
                TagModel tagModel = new TagModel(id, line);
                tagsFromFile.add(tagModel);
                id++;
                entityManager.merge(tagModel);
            }
            return tagsFromFile;
        });
    }

    @Override
    public Map<Long, NewsModel> getNews() {
        return transactionTemplate.execute(s -> {
            List<NewsModel> newsList = entityManager.createQuery("select n from News n").getResultList();
            for (NewsModel item : newsList) {
                news.put(item.getId(), item);
            }
            return news;
        });
    }

    public Map<Long, TagModel> getTags() {
        return transactionTemplate.execute(s -> {
            List<TagModel> tagList = entityManager.createQuery("select t from TagModel t").getResultList();
            for (TagModel item: tagList) {
                tags.put(item.getId(), item);
            }
            return tags;
        });
    }

    @Override
    public Map<Long, AuthorModel> getAuthors() {
        return transactionTemplate.execute(s -> {
            List<AuthorModel> authorList = entityManager.createQuery("select a from AuthorModel a").getResultList();
            for (AuthorModel item : authorList) {
                authors.put(item.getId(), item);
            }
            return authors;
        });
    }

    public AuthorModel getAuthorById(Long id) {
        return transactionTemplate.execute(s -> {
            AuthorModel author= (AuthorModel) entityManager.createQuery("select a from AuthorModel a where id = :id ")
                    .setParameter("id", id).getSingleResult();
                      return author;
        });
    }

    public NewsModel getNewsById(Long id) {
        return transactionTemplate.execute(s -> {
            NewsModel news= (NewsModel) entityManager.createQuery("select n from NewsModel n where id = :id")
                    .setParameter("id", id).getSingleResult();
            return news;
        });
    }

    public TagModel getTagById(Long id) {
        return transactionTemplate.execute(s -> {
            TagModel tag = (TagModel) entityManager.createQuery(SELECT_TAG_BY_ID)
                    .setParameter("id", id).getSingleResult();
            return tag;
        });
    }

    public AuthorModel createAuthor(AuthorModel entity) {
        return transactionTemplate.execute(s -> {
            entityManager.merge(entity);
            AuthorModel createdAuthor = (AuthorModel) entityManager.createQuery("select a from AuthorModel a where id= :id")
                    .setParameter("id", entity.getId())
                    .getSingleResult();
            return createdAuthor;
        });
    }

    public TagModel createTag(TagModel entity) {
        return transactionTemplate.execute(s -> {
            entityManager.merge(entity);
            TagModel createdTag = (TagModel) entityManager.createQuery(SELECT_TAG_BY_ID)
                    .setParameter("id", entity.getId())
                    .getSingleResult();
            return createdTag;
        });
    }

    public AuthorModel updateAuthor(AuthorModel entity) {
        return transactionTemplate.execute(s -> {
            AuthorModel author = entityManager.getReference(AuthorModel.class, entity.getId());
            author.setName(entity.getName());
            author.setLastUpdateDate(entity.getLastUpdateDate());
            entityManager.persist(author);
            entityManager.flush();
            return (AuthorModel) entityManager.createQuery("select a from AuthorModel a where id= :id")
                    .setParameter("id", entity.getId())
                    .getSingleResult();
        });
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
        transactionTemplate.executeWithoutResult(s -> {
            for (int i = 0; i < DEFAULT_NEWS_COUNT_TO_GENERATE; i++) {
                LocalDateTime now = LocalDateTime.now();
                if (contentFromFile.get(i).length() < 255 && titlesFromFile.get(i).length() < 255) {
                    entityManager.merge(new NewsModel((long) i, titlesFromFile.get(i), contentFromFile.get(i), now, now, authorsFromFile.get(i)));
                }
            }
        });

    }

    @Override
    public void afterPropertiesSet() {
        loadNewsToDB("news", "authors", "content", "tag");
    }
}
