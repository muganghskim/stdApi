package com.stdApi.pacificOcean.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProfitandLossDTO {
    private Long plId;
    private int profit;
    private Date createdAt;
}
