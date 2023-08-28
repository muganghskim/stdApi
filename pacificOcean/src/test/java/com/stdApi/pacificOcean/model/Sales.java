package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Sales")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "tidNo")
    private Transaction transaction;

    private int salePrice;

    private String createAt;

    private String updateAt;

    @Builder
    public Sales(Long saleId, Transaction transaction, int salePrice, String createAt, String updateAt) {
        this.saleId = saleId;
        this.transaction = transaction;
        this.salePrice = salePrice;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
