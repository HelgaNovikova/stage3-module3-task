package com.mjc.school.controller.command;

import com.mjc.school.service.dto.AuthorCreateDto;
import com.mjc.school.service.dto.NewsCreateDto;
import com.mjc.school.service.dto.TagCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mapper
public interface DtoMapper {
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    //@Mapping(target = "tagIds")
    NewsCreateDto toNews(Map<String, String> body);

    AuthorCreateDto toAuthors(Map<String, String> body);

    TagCreateDto toTags(Map<String, String> body);

    default List<Long> map(String value) {
        if (value.length() > 0) {
            return Arrays.stream(value.split(",")).map(Long::valueOf).toList();
        } else return Collections.emptyList();
    }
}

