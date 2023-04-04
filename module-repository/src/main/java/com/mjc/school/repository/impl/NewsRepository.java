package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class NewsRepository implements BaseRepository<NewsModel, Long> {

    public static final String SELECT_NEWS_BY_ID = "select n from NewsModel n where id = :id";
    public static final String SELECT_ALL_NEWS = "select n from NewsModel n";
    public static final String DELETE_NEWS_BY_ID = "delete from NewsModel where id = :id";
    private final Map<Long, NewsModel> news = new HashMap<>();
    private final Map<Long, TagModel> tags = new HashMap<>();
    private final Map<Long, AuthorModel> authors = new HashMap<>();
    EntityManager entityManager;
    private TransactionTemplate transactionTemplate;

    @Autowired
    public NewsRepository(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public List<NewsModel> readAll() {
        return transactionTemplate.execute(s -> {
            List<NewsModel> newsList = entityManager.createQuery(SELECT_ALL_NEWS).getResultList();
            for (NewsModel item : newsList) {
                news.put(item.getId(), item);
            }
            return news;
        }).values().stream().toList();
    }

    @Override
    public Optional<NewsModel> readById(Long id) {
        return Optional.ofNullable(transactionTemplate.execute(s ->
                (NewsModel) entityManager.createQuery(SELECT_NEWS_BY_ID)
                .setParameter("id", id).getSingleResult()));
    }

    @Override
    public NewsModel create(NewsModel entity) {
        return save(entity);
    }

    @Override
    public NewsModel update(NewsModel entity) {
        return save(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        transactionTemplate.executeWithoutResult(s -> {
                    entityManager.createQuery(DELETE_NEWS_BY_ID)
                            .setParameter("id", id);
                }
        );
        return existById(id);
    }

    @Override
    public boolean existById(Long id) {
        return 0 < (int) transactionTemplate.execute(s -> {
            return entityManager.createQuery("select count() from NewsModel where id = :id")
                    .setParameter("id", id).getSingleResult();
        });
    }

    public void deleteNewsByAuthorId(Long authorId) {
        List<NewsModel> news = readAll();
        for (NewsModel item : news) {
            if (Objects.equals(item.getAuthor().getId(), authorId)) {
                deleteById(item.getId());
            }
        }
    }

    private NewsModel save(NewsModel news) {
        if (news.getId() == null) {
          //  news.setId(dataSourceFromFile.getNextId());
            news.setCreateDate(LocalDateTime.now());
        }
       // dataSourceFromFile.getNews().put(news.getId(), news);
        news.setLastUpdateDate(LocalDateTime.now());
        return news;
    }
}
