package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.annotation.CommandBody;
import com.mjc.school.controller.annotation.CommandHandler;
import com.mjc.school.controller.annotation.CommandParam;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.AuthorCreateDto;
import com.mjc.school.service.dto.AuthorResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AuthorController implements BaseController<AuthorCreateDto, AuthorResponseDto, Long> {

    private final BaseService<AuthorCreateDto, AuthorResponseDto, Long> authorService;

    @Autowired
    public AuthorController(BaseService<AuthorCreateDto, AuthorResponseDto, Long> authorService) {
        this.authorService = authorService;
    }

    @Override
    @CommandHandler("authors.readAll")
    public List<AuthorResponseDto> readAll() {
        return this.authorService.readAll();
    }

    @Override
    @CommandHandler("authors.readById")
    public AuthorResponseDto readById(@CommandParam("id") Long id) {
        return this.authorService.readById(id);
    }

    @Override
    @CommandHandler("authors.create")
    public AuthorResponseDto create(@CommandBody AuthorCreateDto createRequest) {
        return this.authorService.create(createRequest);
    }

    @Override
    @CommandHandler("authors.update")
    public AuthorResponseDto update(@CommandBody AuthorCreateDto updateRequest) {
        return this.authorService.update(updateRequest);
    }

    @Override
    @CommandHandler("authors.deleteById")
    public boolean deleteById(@CommandParam("id") Long id) {
        return this.authorService.deleteById(id);
    }
}
