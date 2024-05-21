package com.stdApi.pacificOcean.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "OrderItem")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tidNo")
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdNo")
    private Product product;

    private int quantity;

    private int price;

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
    public OrderItem(Long orderItemId, Member member, Transaction transaction, Product product, int quantity, int price ) {
        this.orderItemId = orderItemId;
        this.member = member;
        this.transaction = transaction;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}

