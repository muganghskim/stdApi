package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Inventory")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invenId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "pdNo")
    private Product product;

    private int quantity;

    private String stockType;

    private String createAt;

    private String updateAt;

    @Builder
    public Inventory(Long invenId, Product product, int quantity, String stockType, String createAt, String updateAt) {
        this.invenId = invenId;
        this.product = product;
        this.quantity = quantity;
        this.stockType = stockType;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
