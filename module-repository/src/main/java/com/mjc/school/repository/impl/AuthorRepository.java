package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.AuthorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AuthorRepository implements BaseRepository<AuthorModel, Long> {

    public static final String SELECT_AUTHOR_BY_ID = "select a from AuthorModel a where id= :id";
    public static final String SELECT_ALL_AUTHORS = "select a from AuthorModel a";
    public static final String DELETE_AUTHOR_BY_ID = "delete from AuthorModel where id = :id";
    private final NewsRepository newsRepository;
    private final Map<Long, AuthorModel> authors = new HashMap<>();
    EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public AuthorRepository(EntityManager entityManager, TransactionTemplate transactionTemplate, NewsRepository newsRepository) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
        this.newsRepository = newsRepository;
    }

    public void saveAuthorToDB(AuthorModel author) {
        transactionTemplate.executeWithoutResult(s -> {
            entityManager.merge(author);
        });
    }

    @Override
    public List<AuthorModel> readAll() {
        return transactionTemplate.execute(s -> {
            List<AuthorModel> authorList = entityManager.createQuery(SELECT_ALL_AUTHORS,AuthorModel.class).getResultList();
            for (AuthorModel item : authorList) {
                authors.put(item.getId(), item);
            }
            return authors;
        }).values().stream().toList();
    }

    @Override
    public Optional<AuthorModel> readById(Long id) {
        AuthorModel author = transactionTemplate.execute(s -> entityManager.createQuery(SELECT_AUTHOR_BY_ID,AuthorModel.class)
                .setParameter("id", id).getSingleResult());
        return Optional.ofNullable(author);
    }

    @Override
    public AuthorModel create(AuthorModel entity) {
        setAbsentData(entity);
        return transactionTemplate.execute(s -> entityManager.merge(entity));
    }

    @Override
    public AuthorModel update(AuthorModel entity) {
        setAbsentData(entity);
        return transactionTemplate.execute(s -> {
            AuthorModel author = entityManager.getReference(AuthorModel.class, entity.getId());
            author.setName(entity.getName());
            author.setLastUpdateDate(entity.getLastUpdateDate());
            entityManager.persist(author);
            entityManager.flush();
            return author;
        });
    }

    @Override
    public boolean deleteById(Long id) {
        newsRepository.deleteNewsByAuthorId(id);
        transactionTemplate.executeWithoutResult(s -> {
                    entityManager.createQuery(DELETE_AUTHOR_BY_ID)
                            .setParameter("id", id);
                }
        );
        return existById(id);
    }

    @Override
    public boolean existById(Long id) {
        return 0 < (Long) transactionTemplate.execute(s -> entityManager.createQuery("select count(1) from AuthorModel a where id = :id")
                .setParameter("id", id).getSingleResult());
    }

    private void setAbsentData(AuthorModel author) {
        if (author.getId() == null) {
            author.setCreateDate(LocalDateTime.now());
        }
        author.setLastUpdateDate(LocalDateTime.now());
    }
}
