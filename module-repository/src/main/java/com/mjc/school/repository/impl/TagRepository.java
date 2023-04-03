package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.repository.utils.DataSourceFromDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagRepository implements BaseRepository<TagModel, Long> {

    private final DataSourceFromDB dataSource;

    @Autowired
    public TagRepository(DataSourceFromDB dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<TagModel> readAll() {
        return dataSource.getTags().values().stream().toList();
    }

    @Override
    public Optional<TagModel> readById(Long id) {
        return Optional.ofNullable(dataSource.getTagById(id));
    }

    @Override
    public TagModel create(TagModel entity) {
        return null;
    }

    @Override
    public TagModel update(TagModel entity) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public boolean existById(Long id) {
        return false;
    }
}
