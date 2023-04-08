package com.mjc.school.service.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.NewsMapper;
import com.mjc.school.service.dto.TagCreateDto;
import com.mjc.school.service.dto.TagResponseDto;
import com.mjc.school.service.exception.TagNotFoundException;
import com.mjc.school.service.utils.NewsValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImp implements BaseService<TagCreateDto, TagResponseDto, Long> {

    private final BaseRepository<TagModel, Long> tagRepository;

    public TagServiceImp(BaseRepository<TagModel, Long> tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagResponseDto> readAll() {
        var allTags = tagRepository.readAll();
        List<TagResponseDto> tagDto = new ArrayList<>();
        for (TagModel item : allTags) {
            tagDto.add(NewsMapper.INSTANCE.tagToTagResponseDto(item));
        }
        return tagDto;
    }

    @Override
    public TagResponseDto readById(Long id) {
        Optional<TagModel> tagModel = tagRepository.readById(id);
        TagModel tag = tagModel.orElseThrow(() -> new TagNotFoundException(" Tag with id " + id + " does not exist."));
        return NewsMapper.INSTANCE.tagToTagResponseDto(tag);
    }

    @Override
    public TagResponseDto create(TagCreateDto createRequest) {
        TagModel tagModel = NewsMapper.INSTANCE.createTagDtoToTag(createRequest);
        NewsValidator.validateTag(createRequest.getName());
        return NewsMapper.INSTANCE.tagToTagResponseDto(tagRepository.create(tagModel));
    }

    @Override
    public TagResponseDto update(TagCreateDto updateRequest) {
        if (!tagRepository.existById(updateRequest.getId())) {
            throw new TagNotFoundException(" Tag with id " + updateRequest.getId() + " does not exist.");
        }
        NewsValidator.validateTag(updateRequest.getName());
        TagModel tagModel = NewsMapper.INSTANCE.createTagDtoToTag(updateRequest);
        return NewsMapper.INSTANCE.tagToTagResponseDto(tagRepository.update(tagModel));
    }

    @Override
    public boolean deleteById(Long id) {
        if (!tagRepository.existById(id)) {
            throw new TagNotFoundException(" Tag with id " + id + " does not exist.");
        }
        return tagRepository.deleteById(id);
    }
}
