package com.stdApi.pacificOcean.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Table(name = "Product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdNo;

    private String pdName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    @JsonIgnore
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategoryId")
    @JsonIgnore
    private Subcategory subcategory;

    private String pdDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo")
    private Member member;

    private int pdPrice;

    private String pdStat;

    private String pdSize;

    private String pdImg;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updatedAt;

    // PrePersist is used before the very first time the object is inserted into the database.
    // This will set both createdAt and updatedAt timestamps to the current time when a new entity is created.
    @PrePersist
    protected void onCreate() {
        this.createdAt= new Date();
        this.updatedAt= new Date();
    }

    // PreUpdate is used before any update on the data occurs,
    // so every time an update happens on that row updatedAt will be set to that current timestamp.
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt= new Date();
    }
    @Builder
    public Product(Long pdNo, String pdName, Category category, Subcategory subcategory, String pdDetail, Member member, int pdPrice, String pdStat, String pdSize, String pdImg) {
        this.pdNo = pdNo;
        this.pdName = pdName;
        this.category = category;
        this.subcategory = subcategory;
        this.pdDetail = pdDetail;
        this.member = member;
        this.pdPrice = pdPrice;
        this.pdStat = pdStat;
        this.pdSize = pdSize;
        this.pdImg = pdImg;
    }
}