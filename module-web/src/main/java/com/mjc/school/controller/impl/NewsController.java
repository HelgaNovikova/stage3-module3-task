package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.ExtraNewsController;
import com.mjc.school.controller.annotation.CommandBody;
import com.mjc.school.controller.annotation.CommandHandler;
import com.mjc.school.controller.annotation.CommandParam;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.ExtraNewsService;
import com.mjc.school.service.dto.AuthorResponseDto;
import com.mjc.school.service.dto.NewsCreateDto;
import com.mjc.school.service.dto.NewsResponseDto;
import com.mjc.school.service.dto.TagResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class NewsController implements BaseController<NewsCreateDto, NewsResponseDto, Long>, ExtraNewsController {

    private final BaseService<NewsCreateDto, NewsResponseDto, Long> newsService;
    private final ExtraNewsService extraNewsService;

    @Autowired
    public NewsController(BaseService<NewsCreateDto, NewsResponseDto, Long> newsService, ExtraNewsService extraNewsService) {
        this.newsService = newsService;
        this.extraNewsService = extraNewsService;
    }

    @Override
    @CommandHandler("news.readAll")
    public List<NewsResponseDto> readAll() {
        return this.newsService.readAll();
    }

    @Override
    @CommandHandler("news.readById")
    public NewsResponseDto readById(@CommandParam("id") Long id) {
        return this.newsService.readById(id);
    }

    @Override
    @CommandHandler("news.create")
    public NewsResponseDto create(@CommandBody NewsCreateDto createRequest) {
        return this.newsService.create(createRequest);
    }

    @Override
    @CommandHandler("news.update")
    public NewsResponseDto update(@CommandBody NewsCreateDto updateRequest) {
        return this.newsService.update(updateRequest);
    }

    @Override
    @CommandHandler("news.deleteById")
    public boolean deleteById(@CommandParam("id") Long id) {
        return this.newsService.deleteById(id);
    }

    @Override
    @CommandHandler("news.readAuthorByNewsId")
    public AuthorResponseDto readAuthorByNewsId(@CommandParam("id") Long id) {
        return this.extraNewsService.readAuthorByNewsId(id);
    }

    @Override
    @CommandHandler("news.readTagsByNewsId")
    public List<TagResponseDto> readTagsByNewsId(@CommandParam("id") Long id) {
        return this.extraNewsService.readTagsByNewsId(id);
    }

    @Override
    @CommandHandler("news.readNewsByParams")
    public List<NewsResponseDto> readNewsByParams(@CommandParam("tagId") List<Long> tagId,
                                                  @CommandParam("tagName") String tagName,
                                                  @CommandParam("authorName") String authorName,
                                                  @CommandParam("title") String title,
                                                  @CommandParam("content") String content) {
        return this.extraNewsService.readNewsByParams(tagId, tagName, authorName, title, content);
    }

}
