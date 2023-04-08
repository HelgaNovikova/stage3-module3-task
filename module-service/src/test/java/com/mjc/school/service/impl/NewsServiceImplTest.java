package com.mjc.school.service.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.ExtraNewsRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.dto.NewsCreateDto;
import com.mjc.school.service.dto.NewsResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

public class NewsServiceImplTest {

    private BaseRepository<NewsModel, Long> newsRepository;

    private BaseRepository<AuthorModel, Long> authorRepository;

    private BaseRepository<TagModel, Long> tagRepository;

    private NewsServiceImpl service;

    private NewsModel news;
    private LocalDateTime now;
    private ExtraNewsRepository extraNewsRepository;

    @BeforeEach
    void setUp() {
        newsRepository = mock(BaseRepository.class);
        authorRepository = mock(BaseRepository.class);
        extraNewsRepository = mock(ExtraNewsRepository.class);
        service = new NewsServiceImpl(newsRepository, authorRepository, tagRepository, extraNewsRepository);
        now = LocalDateTime.of(2023, 3, 5, 12, 0, 30);
        news = new NewsModel(1L, "title", "content", now, now, new AuthorModel(1L, "name", now, now));
    }

    @Test
    void getAllNewsDto() {
        //GIVEN
        Mockito.when(newsRepository.readAll()).thenReturn(List.of(news));
        NewsResponseDto expected = getExpectedNewsResponseDto(news.getContent(),
                news.getId(), news.getAuthor().getId(), news.getTitle());
        //WHEN
        var response = service.readAll();
        //THEN
        Mockito.verify(newsRepository).readAll();
        Assertions.assertEquals(List.of(expected), response);
    }

    private NewsResponseDto getExpectedNewsResponseDto(String content, Long id, long authorId, String title) {
        NewsResponseDto expected = new NewsResponseDto();
        expected.setContent(content);
        expected.setId(id);
        expected.setAuthorId(authorId);
        expected.setTitle(title);
        String date = "2023-03-05T12:00:30.000";
        expected.setCreateDate(date);
        expected.setLastUpdateDate(date);
        return expected;
    }

    @Test
    void getNewsByIdDto() {
        //GIVEN
        Mockito.when(newsRepository.readById(1L)).thenReturn(Optional.ofNullable(news));
        NewsResponseDto expected = getExpectedNewsResponseDto(news.getContent(),
                news.getId(), news.getAuthor().getId(), news.getTitle());
        //WHEN
        var response = service.readById(1L);
        //THEN
        Mockito.verify(newsRepository).readById(1L);
        Assertions.assertEquals(expected, response);
    }

    @Test
    void deleteNewsByIdDto() {
        Mockito.when(newsRepository.existById(1L)).thenReturn(true);
        Mockito.when(newsRepository.deleteById(1L)).thenReturn(true);
        var response = service.deleteById(1L);
        Assertions.assertTrue(response);
        Mockito.verify(newsRepository).deleteById(anyLong());
    }

    @Test
    void updatePieceOfNewsByIdDto() {
        //GIVEN
        AuthorModel author = new AuthorModel(1L, "name", now, now);
        NewsCreateDto dto = new NewsCreateDto(1L, "new Title", "new Content", 1L,List.of());
        NewsModel updatedNews = new NewsModel(1L, "new Title", "new Content", now, now,
                author, List.of());
        Mockito.when(newsRepository.update(any())).thenReturn(updatedNews);
        Mockito.when(authorRepository.readById(anyLong())).thenReturn(Optional.of(author));
        Mockito.when(newsRepository.readById(anyLong())).thenReturn(Optional.ofNullable(news));
        NewsResponseDto expected = getExpectedNewsResponseDto(dto.getContent(),
                dto.getId(), dto.getAuthorId(), dto.getTitle());
        //WHEN
        var response = service.update(dto);
        //THEN
        Mockito.verify(newsRepository).update(any());
        Assertions.assertEquals(expected, response);
    }

    @Test
    void createPieceOfNewsDto() {
        //GIVEN
        AuthorModel author = new AuthorModel(1L, "name", now, now);
        NewsCreateDto dto = new NewsCreateDto("new Title", "new Content", 1L, List.of());
        NewsModel createdNews = new NewsModel(1L, "new Title", "new Content", now, now,
                author);
        Mockito.when(newsRepository.create(any())).thenReturn(createdNews);
        Mockito.when(authorRepository.readById(anyLong())).thenReturn(Optional.of(author));
        NewsResponseDto expected = getExpectedNewsResponseDto(dto.getContent(), 1L, dto.getAuthorId(), dto.getTitle());
        //WHEN
        var response = service.create(dto);
        //THEN
        Mockito.verify(newsRepository).create(any());
        Assertions.assertEquals(expected, response);
    }
}
