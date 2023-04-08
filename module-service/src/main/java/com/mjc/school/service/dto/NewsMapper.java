package com.mjc.school.service.dto;

import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface NewsMapper {
    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);
    String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Mapping(target = "lastUpdateDate",
            dateFormat = ISO_FORMAT)
    @Mapping(target = "createDate",
            dateFormat = ISO_FORMAT)
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "tagIds", source = "newsTags")
    NewsResponseDto newsToNewsResponseDto(NewsModel news);

    default Long toId(TagModel dto){
        return dto.getId();
    }

    @Mapping(source = "author", target = "author")
    @Mapping(source = "dto.title", target = "title")
    @Mapping(source = "dto.content", target = "content")
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "newsTags", source = "tags")
    NewsModel createNewsDtoToNews(NewsCreateDto dto, AuthorModel author, List<TagModel> tags);

    AuthorModel createAuthorDtoToAuthor(AuthorCreateDto dto);

    @Mapping(target = "lastUpdateDate",
            dateFormat = ISO_FORMAT)
    @Mapping(target = "createDate",
            dateFormat = ISO_FORMAT)
    AuthorResponseDto authorToAuthorResponseDto(AuthorModel author);

    TagModel createTagDtoToTag(TagCreateDto dto);

    TagResponseDto tagToTagResponseDto(TagModel tag);
}