package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Getter
@Table(name = "Revenue")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long revenueId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "saleNo")
    private Sales sales;

    private String tsfpDay;

    private String tsfpWeek;

    private String tsfpMonth;

    private String tsfpYear;

    private String createAt;

    private String updateAt;

    @Builder
    public Revenue(Long revenueId, Sales sales, String tsfpDay, String tsfpWeek, String tsfpMonth, String tsfpYear,  String createAt, String updateAt) {
        this.revenueId = revenueId;
        this.sales = sales;
        this.tsfpDay = tsfpDay;
        this.tsfpWeek = tsfpWeek;
        this.tsfpMonth = tsfpMonth;
        this.tsfpYear = tsfpYear;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
