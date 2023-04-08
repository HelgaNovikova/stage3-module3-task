package com.mjc.school.service;

import com.mjc.school.service.dto.AuthorResponseDto;
import com.mjc.school.service.dto.NewsResponseDto;
import com.mjc.school.service.dto.TagResponseDto;

import java.util.List;

public interface ExtraNewsService{

    AuthorResponseDto readAuthorByNewsId(Long id);

    List<TagResponseDto> readTagsByNewsId(Long id);

    List<NewsResponseDto> readNewsByParams(List<Long> tagId, String tagName, String authorName, String title, String content);

}
