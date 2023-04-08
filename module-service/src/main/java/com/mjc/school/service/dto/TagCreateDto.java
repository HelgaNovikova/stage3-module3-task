package com.mjc.school.service.dto;

public class TagCreateDto {
    private String name;
    private Long id;

    public TagCreateDto(String name) {
        this.name = name;
    }

    public TagCreateDto() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
