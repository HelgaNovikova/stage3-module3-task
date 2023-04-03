package com.mjc.school.service.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.AuthorCreateDto;
import com.mjc.school.service.dto.AuthorResponseDto;
import com.mjc.school.service.dto.NewsMapper;
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
public class AuthorServiceImpl implements BaseService<AuthorCreateDto, AuthorResponseDto, Long> {


    private final BaseRepository<AuthorModel, Long> authorRepository;

    @Autowired
    public AuthorServiceImpl(BaseRepository<AuthorModel, Long> authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorResponseDto> readAll() {
        var allAuthors = authorRepository.readAll();
        List<AuthorResponseDto> authorDto = new ArrayList<>();
        for (AuthorModel item : allAuthors) {
            authorDto.add(NewsMapper.INSTANCE.authorToAuthorResponseDto(item));
        }
        return authorDto;
    }

    @Override
    public AuthorResponseDto readById(Long id) {
        Optional<AuthorModel> author = authorRepository.readById(id);
        AuthorModel authorModel = author.orElseThrow(() -> new NewsNotFoundException(" Author with id " + id + " does not exist."));
        return NewsMapper.INSTANCE.authorToAuthorResponseDto(authorModel);
    }

    @Override
    public AuthorResponseDto create(AuthorCreateDto createRequest) {
        AuthorModel authorModel = NewsMapper.INSTANCE.createAuthorDtoToAuthor(createRequest);
        NewsValidator.validateAuthor(createRequest.getName());
        return NewsMapper.INSTANCE.authorToAuthorResponseDto(authorRepository.create(authorModel));
    }

    @Override
    public AuthorResponseDto update(AuthorCreateDto updateRequest) {
        Optional<AuthorModel> author = authorRepository.readById(updateRequest.getId());
        LocalDateTime createDate = author.orElseThrow(() -> new AuthorNotFoundException(" Author with id " + updateRequest.getId() + " does not exist.")).getCreateDate();
        AuthorModel authorModel = NewsMapper.INSTANCE.createAuthorDtoToAuthor(updateRequest);
        authorModel.setCreateDate(createDate);
        NewsValidator.validateAuthor(updateRequest.getName());
        return NewsMapper.INSTANCE.authorToAuthorResponseDto(authorRepository.update(authorModel));
    }

    @Override
    public boolean deleteById(Long id) {
        if (!authorRepository.existById(id)) {
            throw new NewsNotFoundException(" Author with id " + id + " does not exist.");
        }
        return authorRepository.deleteById(id);
    }
}
