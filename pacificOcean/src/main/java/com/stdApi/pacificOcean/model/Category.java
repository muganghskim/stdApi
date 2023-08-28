package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;

    private String createAt;

    private String updateAt;

    @Builder
    public Category(Long categoryId, String categoryName, String createAt, String updateAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
