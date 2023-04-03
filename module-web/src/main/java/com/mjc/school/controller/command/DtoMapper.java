package com.mjc.school.controller.command;

import com.mjc.school.service.dto.AuthorCreateDto;
import com.mjc.school.service.dto.NewsCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface DtoMapper {
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    NewsCreateDto toNews(Map<String, String> body);

    AuthorCreateDto toAuthors(Map<String, String> body);
}
