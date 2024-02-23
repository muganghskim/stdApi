package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
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

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
}
