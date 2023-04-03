package com.mjc.school.service.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.service.dto.AuthorCreateDto;
import com.mjc.school.service.dto.AuthorResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class AuthorServiceImplTest {

    private BaseRepository<AuthorModel, Long> authorRepository;

    private AuthorServiceImpl service;

    private AuthorModel author;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        authorRepository = Mockito.mock(BaseRepository.class);
        service = new AuthorServiceImpl(authorRepository);
        now = LocalDateTime.of(2023, 3, 5, 12, 0, 30);
        author = new AuthorModel(1L, "name", now, now);
    }

    @Test
    void getAllAuthorsDto() {
        //GIVEN
        Mockito.when(authorRepository.readAll()).thenReturn(List.of(author));
        AuthorResponseDto expected = getExpectedAuthorResponseDto(author.getId(), author.getName());
        //WHEN
        var response = service.readAll();
        //THEN
        Mockito.verify(authorRepository).readAll();
        Assertions.assertEquals(List.of(expected), response);
    }

    private AuthorResponseDto getExpectedAuthorResponseDto(Long id, String name) {
        String date = "2023-03-05T12:00:30.000";
        AuthorResponseDto expected = new AuthorResponseDto(id, name, date, date);
        expected.setCreateDate(date);
        expected.setLastUpdateDate(date);
        return expected;
    }

    @Test
    void getAuthorByIdDto() {
        //GIVEN
        Mockito.when(authorRepository.readById(1L)).thenReturn(Optional.ofNullable(author));
        AuthorResponseDto expected = getExpectedAuthorResponseDto(author.getId(), author.getName());
        //WHEN
        var response = service.readById(1L);
        //THEN
        Mockito.verify(authorRepository).readById(1L);
        Assertions.assertEquals(expected, response);
    }

    @Test
    void deleteAuthorByIdDto() {
        Mockito.when(authorRepository.readById(1L)).thenReturn(Optional.ofNullable(author));
        Mockito.when(authorRepository.deleteById(1L)).thenReturn(true);
        var response = service.deleteById(1L);
        Assertions.assertTrue(response);
        Mockito.verify(authorRepository).deleteById(anyLong());
    }

    @Test
    void updateAuthorByIdDto() {
        //GIVEN
        AuthorModel author = new AuthorModel(1L, "name", now, now);
        AuthorCreateDto dto = new AuthorCreateDto(1L, "new name", now, now);
        AuthorModel updatedAuthor = new AuthorModel(1L, "new name", now, now);
        Mockito.when(authorRepository.update(any())).thenReturn(updatedAuthor);
        Mockito.when(authorRepository.readById(anyLong())).thenReturn(Optional.of(author));
        AuthorResponseDto expected = getExpectedAuthorResponseDto(dto.getId(), dto.getName());
        //WHEN
        var response = service.update(dto);
        //THEN
        Mockito.verify(authorRepository).update(any());
        Assertions.assertEquals(expected, response);
    }

    @Test
    void createAuthorDto() {
        //GIVEN
        AuthorModel author = new AuthorModel(1L, "name", now, now);
        AuthorCreateDto dto = new AuthorCreateDto(1L, "new name", now, now);
        AuthorModel createdAuthor = new AuthorModel(1L, "new name", now, now);
        Mockito.when(authorRepository.create(any())).thenReturn(createdAuthor);
        Mockito.when(authorRepository.readById(anyLong())).thenReturn(Optional.of(author));
        AuthorResponseDto expected = getExpectedAuthorResponseDto(dto.getId(), dto.getName());
        //WHEN
        var response = service.create(dto);
        //THEN
        Mockito.verify(authorRepository).create(any());
        Assertions.assertEquals(expected, response);
    }
}
