package com.mjc.school.service.dto;

import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NewsMapper {
    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);
    String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Mapping(target = "lastUpdateDate",
            dateFormat = ISO_FORMAT)
    @Mapping(target = "createDate",
            dateFormat = ISO_FORMAT)
    @Mapping(target = "authorId", source = "author.id")
    NewsResponseDto newsToNewsResponseDto(NewsModel pieceOfNews);

    @Mapping(source = "author", target = "author")
    @Mapping(source = "dto.title", target = "title")
    @Mapping(source = "dto.content", target = "content")
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    NewsModel createNewsDtoToNews(NewsCreateDto dto, AuthorModel author);

    AuthorModel createAuthorDtoToAuthor(AuthorCreateDto dto);

    @Mapping(target = "lastUpdateDate",
            dateFormat = ISO_FORMAT)
    @Mapping(target = "createDate",
            dateFormat = ISO_FORMAT)
    AuthorResponseDto authorToAuthorResponseDto(AuthorModel author);
}