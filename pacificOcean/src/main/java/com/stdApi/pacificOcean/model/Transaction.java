package com.stdApi.pacificOcean.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo")
    private Member member;

    @OneToMany(mappedBy = "transaction")
    private List<OrderItem> orderItems = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "deliveryId")
//    private Delivery delivery;

    private String address1;
    private String address2;
    private String address3;

    private String rcvName;

    private String rcvPhn;

    private String tidStat;

    private String paymentMethod;

    private int totalAmount;

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
    public Transaction(Long tid, Member member, String address1, String address2, String address3, String rcvName, String rcvPhn, String tidStat, String paymentMethod) {
        this.tid = tid;
        this.member = member;
//        this.delivery = delivery;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.rcvName = rcvName;
        this.rcvPhn = rcvPhn;
        this.tidStat = tidStat;
        this.paymentMethod = paymentMethod;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setTransaction(this);
        this.totalAmount += orderItem.getPrice() * orderItem.getQuantity();
    }


}