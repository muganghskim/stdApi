package com.stdApi.pacificOcean.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Setter
@Getter
public class CartDTO {

    private Long cartId;
    private Long pdNo;
    private int pdQuantity;
    private String pdName;
    private String pdImg;
    private int pdPrice;
}
