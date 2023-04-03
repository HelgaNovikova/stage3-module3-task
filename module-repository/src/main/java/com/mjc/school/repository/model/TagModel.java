package com.mjc.school.repository.model;

import javax.persistence.*;

@Entity
@Table(name = "Tag")
public class TagModel implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    public TagModel(Long id, String name) {
        this.id = id;
        this.name = name;
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
