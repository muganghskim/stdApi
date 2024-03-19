package com.stdApi.pacificOcean.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Setter
@Getter
public class ProductDTO {
    private Long pdNo;

    private String pdName;

    private String categoryId;

    private String subcategoryId;

    private String pdDetail;

    private int pdPrice;

    private String pdStat;

    private String pdSize;

    private String pdImg;

    private int pdQuantity;
}
