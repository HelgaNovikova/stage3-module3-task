package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.utils.DataSourceFromDB;
import com.mjc.school.repository.utils.DataSourceFromFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepository implements BaseRepository<AuthorModel, Long> {

    //private final DataSourceFromFile dataSource;
  private final DataSourceFromDB dataSource;
    private final NewsRepository newsRepository;

//    @Autowired
//    public AuthorRepository(DataSourceFromFile ds, NewsRepository newsRepository) {
//        this.dataSourceFromFile = ds;
//        this.newsRepository = newsRepository;
//    }

    @Autowired
    public AuthorRepository(DataSourceFromDB ds, NewsRepository newsRepository) {
        this.dataSource = ds;
        this.newsRepository = newsRepository;
    }

    @Override
    public List<AuthorModel> readAll() {
        return dataSource.getAuthors().values().stream().toList();
    }

//    @Override
//    public Optional<AuthorModel> readById(Long id) {
//        return Optional.ofNullable(dataSource.getAuthors().get(id));
//    }

        @Override
    public Optional<AuthorModel> readById(Long id) {
        return Optional.ofNullable(dataSource.getAuthorById(id));
    }

    @Override
    public AuthorModel create(AuthorModel entity) {
        return save(entity);
    }

    @Override
    public AuthorModel update(AuthorModel entity) {
        return save(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        newsRepository.deleteNewsByAuthorId(id);
        return dataSource.getAuthors().remove(id) != null;
    }

    @Override
    public boolean existById(Long id) {
        return dataSource.getAuthors().get(id) != null;
    }

    private AuthorModel save(AuthorModel author) {
        if (author.getId() == null) {
            //author.setId(dataSource.getNextId());
            author.setCreateDate(LocalDateTime.now());
        }
        dataSource.getAuthors().put(author.getId(), author);
        author.setLastUpdateDate(LocalDateTime.now());
        return author;
    }
}
