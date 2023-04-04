package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.TagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TagRepository implements BaseRepository<TagModel, Long> {

    public static final String SELECT_ALL_TAGS = "select t from TagModel t";
    public static final String SELECT_TAG_BY_ID = "select t from TagModel t where id = :id";
    public static final String DELETE_TAG_BY_ID = "delete from TagModel where id = :id";
    public static final String TAG_EXISTS_BY_ID = "select count() from TagModel where id = :id";
    private final Map<Long, TagModel> tags = new HashMap<>();
    EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    public void saveTagToDB(TagModel tag) {
        transactionTemplate.executeWithoutResult(s -> {
            entityManager.merge(tag);
        });
    }

    @Autowired
    public TagRepository(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public List<TagModel> readAll() {
        return transactionTemplate.execute(s -> {
            List<TagModel> tagList = entityManager.createQuery(SELECT_ALL_TAGS).getResultList();
            for (TagModel item : tagList) {
                tags.put(item.getId(), item);
            }
            return tags;
        }).values().stream().toList();
    }

    @Override
    public Optional<TagModel> readById(Long id) {
        TagModel tag = transactionTemplate.execute(s -> (TagModel) entityManager.createQuery(SELECT_TAG_BY_ID)
                .setParameter("id", id).getSingleResult());
        return Optional.ofNullable(tag);
    }

    @Override
    public TagModel create(TagModel entity) {
        return transactionTemplate.execute(s -> {
            entityManager.merge(entity);
            TagModel createdTag = (TagModel) entityManager.createQuery(SELECT_TAG_BY_ID)
                    .setParameter("id", entity.getId())
                    .getSingleResult();
            return createdTag;
        });
    }

    @Override
    public TagModel update(TagModel entity) {
        return transactionTemplate.execute(s -> {
            TagModel tag = entityManager.getReference(TagModel.class, entity.getId());
            entityManager.persist(tag);
            entityManager.flush();
            return (TagModel) entityManager.createQuery(SELECT_TAG_BY_ID)
                    .setParameter("id", entity.getId())
                    .getSingleResult();
        });
    }

    @Override
    public boolean deleteById(Long id) {
        transactionTemplate.executeWithoutResult(s -> {
                    entityManager.createQuery(DELETE_TAG_BY_ID)
                            .setParameter("id", id);
                }
        );
        return existById(id);
    }

    @Override
    public boolean existById(Long id) {
        return 0 < (int) transactionTemplate.execute(s -> {
            return entityManager.createQuery(TAG_EXISTS_BY_ID)
                    .setParameter("id", id).getSingleResult();
        });
    }
}
