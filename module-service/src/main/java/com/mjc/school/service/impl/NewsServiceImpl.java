package com.mjc.school.service.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.ExtraNewsRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.ExtraNewsService;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.exception.AuthorNotFoundException;
import com.mjc.school.service.exception.NewsNotFoundException;
import com.mjc.school.service.exception.TagNotFoundException;
import com.mjc.school.service.utils.NewsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements ExtraNewsService, BaseService<NewsCreateDto, NewsResponseDto, Long> {

    private final BaseRepository<NewsModel, Long> newsRepository;

    private final BaseRepository<AuthorModel, Long> authorRepository;
    private final BaseRepository<TagModel, Long> tagRepository;
    private final ExtraNewsRepository extraNewsRepository;

    @Autowired
    public NewsServiceImpl(BaseRepository<NewsModel, Long> newsRepository, BaseRepository<AuthorModel, Long> authorRepository, BaseRepository<TagModel, Long> tagRepository, ExtraNewsRepository extraNewsRepository) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.extraNewsRepository = extraNewsRepository;
    }

    @Override
    @Transactional
    public List<NewsResponseDto> readAll() {
        var allNews = newsRepository.readAll();
        List<NewsResponseDto> newsDto = new ArrayList<>();
        for (NewsModel item : allNews) {
            newsDto.add(NewsMapper.INSTANCE.newsToNewsResponseDto(item));
        }
        return newsDto;
    }

    @Override
    @Transactional
    public NewsResponseDto readById(Long id) {
        Optional<NewsModel> news = newsRepository.readById(id);
        NewsModel newsModel = news.orElseThrow(() -> new NewsNotFoundException(" News with id " + id + " does not exist."));
        return NewsMapper.INSTANCE.newsToNewsResponseDto(newsModel);
    }

    @Override
    @Transactional
    public NewsResponseDto create(NewsCreateDto createRequest) {
        Optional<AuthorModel> author = authorRepository.readById(createRequest.getAuthorId());
        AuthorModel authorModel = author.orElseThrow(() -> new AuthorNotFoundException(" Author with id " + createRequest.getAuthorId() + " does not exist."));

        List<TagModel> tagModels = createRequest.getTagIds().stream().map(s -> tagRepository.readById(s)
                .orElseThrow(() -> new TagNotFoundException("Tag with id " + s + " does not exist."))).toList();

        NewsModel news = NewsMapper.INSTANCE.createNewsDtoToNews(createRequest, authorModel, tagModels);
        NewsValidator.validateContent(news.getContent());
        NewsValidator.validateTitle(news.getTitle());
        return NewsMapper.INSTANCE.newsToNewsResponseDto(newsRepository.create(news));
    }

    @Override
    @Transactional
    public NewsResponseDto update(NewsCreateDto updateRequest) {
        NewsValidator.validateContent(updateRequest.getContent());
        NewsValidator.validateTitle(updateRequest.getTitle());
        Optional<AuthorModel> author = authorRepository.readById(updateRequest.getAuthorId());
        Optional<NewsModel> news = newsRepository.readById(updateRequest.getId());

        List<TagModel> tagModels = updateRequest.getTagIds().stream().map(s -> tagRepository.readById(s)
                .orElseThrow(() -> new TagNotFoundException("Tag with id " + s + " does not exist."))).toList();

        LocalDateTime createDate = news.orElseThrow(() -> new NewsNotFoundException(" News with id " + updateRequest.getId() + " does not exist.")).getCreateDate();
        AuthorModel authorModel = author.orElseThrow(() -> new AuthorNotFoundException(" Author with id " + updateRequest.getAuthorId() + " does not exist."));
        NewsModel newsModel = NewsMapper.INSTANCE.createNewsDtoToNews(updateRequest, authorModel, tagModels);
        newsModel.setCreateDate(createDate);
        newsModel.setId(updateRequest.getId());
        return NewsMapper.INSTANCE.newsToNewsResponseDto(newsRepository.update(newsModel));
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if (!newsRepository.existById(id)) {
            throw new NewsNotFoundException(" News with id " + id + " does not exist.");
        }
        return newsRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AuthorResponseDto readAuthorByNewsId(Long id) {
        Optional<NewsModel> news = newsRepository.readById(id);
        NewsModel newsModel = news.orElseThrow(() -> new NewsNotFoundException(" News with id " + id + " does not exist."));
        AuthorModel author = newsModel.getAuthor();
        return NewsMapper.INSTANCE.authorToAuthorResponseDto(author);
    }

    @Override
    @Transactional
    public List<TagResponseDto> readTagsByNewsId(Long id) {
        Optional<NewsModel> news = newsRepository.readById(id);
        NewsModel newsModel = news.orElseThrow(() -> new NewsNotFoundException(" News with id " + id + " does not exist."));
        List<TagModel> tags = newsModel.getNewsTags();
        List<TagResponseDto> tagDto = new ArrayList<>();
        for (TagModel item : tags) {
            tagDto.add(NewsMapper.INSTANCE.tagToTagResponseDto(item));
        }
        return tagDto;
    }

    @Override
    @Transactional
    public List<NewsResponseDto> readNewsByParams(List<Long> tagId, String tagName, String authorName, String title, String content) {
        List<NewsModel> news = extraNewsRepository.readNewsByParams(tagId, tagName, authorName, title, content);
        List<NewsResponseDto> response = new ArrayList<>();
        for (NewsModel item: news) {
            response.add(NewsMapper.INSTANCE.newsToNewsResponseDto(item));
        }
        return response;
    }
}
