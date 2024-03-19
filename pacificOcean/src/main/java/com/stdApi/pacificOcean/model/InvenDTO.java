package com.stdApi.pacificOcean.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
public class InvenDTO {
    private Long invenId;

    private String pdName;

    private int quantity;

    private String stockType;

    private Date createdAt;
}
