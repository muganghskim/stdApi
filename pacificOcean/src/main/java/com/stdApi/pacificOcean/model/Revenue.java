package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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
    public Revenue(Long revenueId, Sales sales, String tsfpDay, String tsfpWeek, String tsfpMonth, String tsfpYear) {
        this.revenueId = revenueId;
        this.sales = sales;
        this.tsfpDay = tsfpDay;
        this.tsfpWeek = tsfpWeek;
        this.tsfpMonth = tsfpMonth;
        this.tsfpYear = tsfpYear;
    }
}
