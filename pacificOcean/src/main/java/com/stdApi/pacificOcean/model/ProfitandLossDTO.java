package com.stdApi.pacificOcean.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProfitandLossDTO {
    private Long plId;
    private int profit;
    private int salaries;
    private int bills;
    private int taxes;
    private int refund;
    private Date createdAt;
}
