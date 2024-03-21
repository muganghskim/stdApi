package com.stdApi.pacificOcean.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Setter
@Getter
public class SupportDTO {
    private Long supportId;
    private String userEmail;
    private String question;
    private String answer;
    private String stat;
    private Date createdAt;
    private Date updatedAt;
}
