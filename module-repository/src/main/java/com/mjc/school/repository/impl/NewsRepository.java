package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.ExtraNewsRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class NewsRepository implements BaseRepository<NewsModel, Long>, ExtraNewsRepository {

    public static final String SELECT_ALL_NEWS = "select n from NewsModel n";
    public static final String DELETE_NEWS_BY_ID = "delete from NewsModel where id = :id";
    private final TransactionTemplate transactionTemplate;
    EntityManager entityManager;

    @Autowired
    public NewsRepository(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
    }

    public void saveNewsToDB(NewsModel news) {
        transactionTemplate.executeWithoutResult(s -> entityManager.merge(news));
    }

    @Override
    public List<NewsModel> readAll() {
        return entityManager.createQuery(SELECT_ALL_NEWS, NewsModel.class).getResultList();
    }

    @Override
    public Optional<NewsModel> readById(Long id) {
        NewsModel news = entityManager.find(NewsModel.class, id);
        return Optional.ofNullable(news);
    }

    @Override
    public NewsModel create(NewsModel entity) {
        entity.setCreateDate(LocalDateTime.now());
        entity.setLastUpdateDate(LocalDateTime.now());
        return entityManager.merge(entity);
    }

    @Override
    public NewsModel update(NewsModel entity) {
        entityManager.detach(entity);
        entity.setLastUpdateDate(LocalDateTime.now());
        return entityManager.merge(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        entityManager.createQuery(DELETE_NEWS_BY_ID).setParameter("id", id).executeUpdate();
        return !existById(id);
    }

    @Override
    public boolean existById(Long id) {
        return readById(id).isPresent();
    }

    public void deleteNewsByAuthorId(Long authorId) {
        List<NewsModel> news = readAll();
        for (NewsModel item : news) {
            if (Objects.equals(item.getAuthor().getId(), authorId)) {
                deleteById(item.getId());
            }
        }
    }

    @Override
    public List<NewsModel> readNewsByParams(List<Long> tagId, String tagName, String authorName, String title, String content) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<NewsModel> cq = cb.createQuery(NewsModel.class);
        Root<NewsModel> news = cq.from(NewsModel.class);
        CriteriaQuery<NewsModel> select = cq.select(news);
        if (!ObjectUtils.isEmpty(title)) {
            select.where(cb.like(news.get("title"), "%" + title + "%"));
        }
        if (!ObjectUtils.isEmpty(content)) {
            select.where(cb.like(news.get("content"), "%" + content + "%"));
        }
        if (!ObjectUtils.isEmpty(authorName)) {
            Join<NewsModel, AuthorModel> authorJoin = news.join("author", JoinType.INNER);
            select.where(cb.like(authorJoin.get("name"), "%" + authorName + "%"));
        }
        if (!ObjectUtils.isEmpty(tagName)) {
            Join<NewsModel, TagModel> tagJoin = news.join("newsTags", JoinType.INNER);
            select.where(cb.like(tagJoin.get("name"), "%" + tagName + "%"));
        }
        if (!ObjectUtils.isEmpty(tagId)) {
            Join<NewsModel, TagModel> tagJoin = news.join("newsTags", JoinType.INNER);
            select.where(tagJoin.get("id").in(tagId)).distinct(true);
        }

        Query q = entityManager.createQuery(select);
        return (List<NewsModel>) q.getResultList();
    }
}
