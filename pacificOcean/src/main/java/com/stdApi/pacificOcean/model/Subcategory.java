package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Subcategory")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subcategoryId;

    private String subcategoryName;

    @Builder
    public Subcategory(Long subcategoryId, String subcategoryName) {
        this.subcategoryId = subcategoryId;
        this.subcategoryName = subcategoryName;
    }
}
