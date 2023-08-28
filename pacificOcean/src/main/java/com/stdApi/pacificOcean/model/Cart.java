package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Cart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "pdNo")
    private Product product;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userNo")
    private Member member;

    private String createAt;

    private String updateAt;

    @Builder
    public Cart(Long cartId, Product product, int quantity, Member member, String createAt, String updateAt) {
        this.cartId = cartId;
        this.product = product;
        this.quantity = quantity;
        this.member = member;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
