package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.TagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepository implements BaseRepository<TagModel, Long> {

    public static final String SELECT_ALL_TAGS = "select t from TagModel t";
    public static final String DELETE_TAG_BY_ID = "delete from TagModel where id = :id";
    public static final String UPDATE_TAG = "update TagModel set name = :name where id= :id";
    private final TransactionTemplate transactionTemplate;
    EntityManager entityManager;

    @Autowired
    public TagRepository(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
    }

    public void saveTagToDB(TagModel tag) {
        transactionTemplate.executeWithoutResult(s ->
                entityManager.merge(tag)
        );
    }

    @Override
    public List<TagModel> readAll() {
        return transactionTemplate.execute(s -> entityManager.createQuery(SELECT_ALL_TAGS, TagModel.class).getResultList());
    }

    @Override
    public Optional<TagModel> readById(Long id) {
        TagModel tag = entityManager.find(TagModel.class, id);
        return Optional.ofNullable(tag);
    }

    @Override
    public TagModel create(TagModel entity) {
        return transactionTemplate.execute(s -> entityManager.merge(entity));
    }

    @Override
    public TagModel update(TagModel entity) {
        return transactionTemplate.execute(s -> {
            entityManager.createQuery(UPDATE_TAG)
                    .setParameter("id", entity.getId())
                    .setParameter("name", entity.getName())
                    .executeUpdate();
            return entity;
        });
    }

    @Override
    public boolean deleteById(Long id) {
        transactionTemplate.executeWithoutResult(s -> entityManager.createQuery(DELETE_TAG_BY_ID)
                .setParameter("id", id).executeUpdate()
        );
        return !existById(id);
    }

    @Override
    public boolean existById(Long id) {
        return readById(id).isPresent();
    }
}
