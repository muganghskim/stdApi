package com.stdApi.pacificOcean.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class OrderItemDTO {
    private Long orderItemId;
    private String pdName;
    private int quantity;
    private int price;
    private String userEmail;
    private String address1;
    private String address2;
    private String address3;
    private String tidStat;
    private Date createdAt;
    private Date updatedAt;
}
