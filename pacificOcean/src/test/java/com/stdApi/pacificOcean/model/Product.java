package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdNo;

    private String pdName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "categoryNo")
    private Category category;

    private String pdDetail;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userNo")
    private Member member;

    private int pdPrice;

    private String pdStat;

    private String pdSize;

    private String pdImg;

    private String createAt;

    private String updateAt;
    @Builder
    public Product(Long pdNo, String pdName, Category category, String pdDetail, Member member, int pdPrice, String pdStat, String pdSize, String pdImg, String createAt, String updateAt) {
        this.pdNo = pdNo;
        this.pdName = pdName;
        this.category = category;
        this.pdDetail = pdDetail;
        this.member = member;
        this.pdPrice = pdPrice;
        this.pdStat = pdStat;
        this.pdSize = pdSize;
        this.pdImg = pdImg;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}