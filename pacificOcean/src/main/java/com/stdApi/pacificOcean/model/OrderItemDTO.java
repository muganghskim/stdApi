package com.stdApi.pacificOcean.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemDTO {
    private Long orderItemId;
    private int quantity;
    private int price;
}
