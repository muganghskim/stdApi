package com.stdApi.pacificOcean.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "ProfitandLoss")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ProfitandLoss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revenueId")
    private Revenue revenue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenseId")
    private Expenses expenses;

    private int profit;

    private int totalProfit;

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
    public ProfitandLoss(Long plId, Revenue revenue, Expenses expenses, int profit, int totalProfit) {
        this.plId = plId;
        this.revenue = revenue;
        this.expenses = expenses;
        this.profit = profit;
        this.totalProfit = totalProfit;
    }
}
