package com.mjc.school.service.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.NewsCreateDto;
import com.mjc.school.service.dto.NewsMapper;
import com.mjc.school.service.dto.NewsResponseDto;
import com.mjc.school.service.exception.AuthorNotFoundException;
import com.mjc.school.service.exception.NewsNotFoundException;
import com.mjc.school.service.utils.NewsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements BaseService<NewsCreateDto, NewsResponseDto, Long> {

    private final BaseRepository<NewsModel, Long> newsRepository;

    private final BaseRepository<AuthorModel, Long> authorRepository;

    @Autowired
    public NewsServiceImpl(BaseRepository<NewsModel, Long> newsRepository, BaseRepository<AuthorModel, Long> authorRepository) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public List<NewsResponseDto> readAll() {
        var allNews = newsRepository.readAll();
        List<NewsResponseDto> newsDto = new ArrayList<>();
        for (NewsModel item : allNews) {
            newsDto.add(NewsMapper.INSTANCE.newsToNewsResponseDto(item));
        }
        return newsDto;
    }

    @Override
    public NewsResponseDto readById(Long id) {
        Optional<NewsModel> news = newsRepository.readById(id);
        NewsModel newsModel = news.orElseThrow(() -> new NewsNotFoundException(" News with id " + id + " does not exist."));
        return NewsMapper.INSTANCE.newsToNewsResponseDto(newsModel);
    }

    @Override
    public NewsResponseDto create(NewsCreateDto createRequest) {
        Optional<AuthorModel> author = authorRepository.readById(createRequest.getAuthorId());
        AuthorModel authorModel = author.orElseThrow(() -> new AuthorNotFoundException(" Author with id " + createRequest.getAuthorId() + " does not exist."));
        NewsModel news = NewsMapper.INSTANCE.createNewsDtoToNews(createRequest, authorModel);
        NewsValidator.validateContent(news.getContent());
        NewsValidator.validateTitle(news.getTitle());
        return NewsMapper.INSTANCE.newsToNewsResponseDto(newsRepository.create(news));
    }

    @Override
    public NewsResponseDto update(NewsCreateDto updateRequest) {
        Optional<AuthorModel> author = authorRepository.readById(updateRequest.getAuthorId());
        Optional<NewsModel> news = newsRepository.readById(updateRequest.getId());
        LocalDateTime createDate = news.orElseThrow(() -> new NewsNotFoundException(" News with id " + updateRequest.getId() + " does not exist.")).getCreateDate();
        AuthorModel authorModel = author.orElseThrow(() -> new AuthorNotFoundException(" Author with id " + updateRequest.getAuthorId() + " does not exist."));
        NewsModel newsModel = NewsMapper.INSTANCE.createNewsDtoToNews(updateRequest, authorModel);
        newsModel.setCreateDate(createDate);
        newsModel.setId(updateRequest.getId());
        NewsValidator.validateContent(updateRequest.getContent());
        NewsValidator.validateTitle(updateRequest.getTitle());
        return NewsMapper.INSTANCE.newsToNewsResponseDto(newsRepository.update(newsModel));
    }

    @Override
    public boolean deleteById(Long id) {
        if (!newsRepository.existById(id)) {
            throw new NewsNotFoundException(" News with id " + id + " does not exist.");
        }
        return newsRepository.deleteById(id);
    }
}
