package com.mjc.school.repository.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Tag")
public class TagModel implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "newsTags")
    private List<NewsModel> newsModel;

    public TagModel(Long id, String name) {
        this.id = id;
        this.name = name;
        this.newsModel = null;
    }

    public TagModel(Long id, String name, List<NewsModel> newsModel) {
        this.id = id;
        this.name = name;
        this.newsModel = newsModel;
    }

    public TagModel() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
