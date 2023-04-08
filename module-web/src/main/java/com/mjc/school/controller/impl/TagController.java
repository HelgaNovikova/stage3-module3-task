package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.annotation.CommandBody;
import com.mjc.school.controller.annotation.CommandHandler;
import com.mjc.school.controller.annotation.CommandParam;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.TagCreateDto;
import com.mjc.school.service.dto.TagResponseDto;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TagController implements BaseController<TagCreateDto, TagResponseDto, Long> {

    private final BaseService<TagCreateDto, TagResponseDto, Long> tagService;

    public TagController(BaseService<TagCreateDto, TagResponseDto, Long> tagService) {
        this.tagService = tagService;
    }

    @Override
    @CommandHandler("tags.readAll")
    public List<TagResponseDto> readAll() {
        return this.tagService.readAll();
    }

    @Override
    @CommandHandler("tags.readById")
    public TagResponseDto readById(@CommandParam("id") Long id) {
        return this.tagService.readById(id);
    }

    @Override
    @CommandHandler("tags.create")
    public TagResponseDto create(@CommandBody TagCreateDto createRequest) {
        return this.tagService.create(createRequest);
    }

    @Override
    @CommandHandler("tags.update")
    public TagResponseDto update(@CommandBody TagCreateDto updateRequest) {
        return this.tagService.update(updateRequest);
    }

    @Override
    @CommandHandler("tags.deleteById")
    public boolean deleteById(@CommandParam("id") Long id) {
        return this.tagService.deleteById(id);
    }
}
