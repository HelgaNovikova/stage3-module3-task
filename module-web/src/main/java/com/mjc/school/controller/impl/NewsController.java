package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.annotation.CommandBody;
import com.mjc.school.controller.annotation.CommandHandler;
import com.mjc.school.controller.annotation.CommandParam;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.NewsCreateDto;
import com.mjc.school.service.dto.NewsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class NewsController implements BaseController<NewsCreateDto, NewsResponseDto, Long> {

    private final BaseService<NewsCreateDto, NewsResponseDto, Long> newsService;

    @Autowired
    public NewsController(BaseService<NewsCreateDto, NewsResponseDto, Long> newsService) {
        this.newsService = newsService;
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

}
