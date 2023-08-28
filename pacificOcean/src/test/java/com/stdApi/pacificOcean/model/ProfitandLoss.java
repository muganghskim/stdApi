package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "ProfitandLoss")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfitandLoss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "revenueNo")
    private Revenue revenue;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "expenseNo")
    private Expenses expenses;

    private int totalProfit;

    private String createAt;

    private String updateAt;

    @Builder
    public ProfitandLoss(Long plId, Revenue revenue, Expenses expenses, int totalProfit, String createAt, String updateAt) {
        this.plId = plId;
        this.revenue = revenue;
        this.expenses = expenses;
        this.totalProfit = totalProfit;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
