package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Expenses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    private int salaries;

    private int bills;

    private int taxes;

    private String createAt;

    private String updateAt;

    @Builder
    public Expenses(Long expenseId, int salaries, int bills, int taxes, String createAt, String updateAt) {
        this.expenseId = expenseId;
        this.salaries = salaries;
        this.bills = bills;
        this.taxes = taxes;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
