package com.stdApi.pacificOcean.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
public class NoticeDTO {

    private Long noticeId;

    private String title;

    private String content;

    private String noticeType;

    private String userEmail;

    private String userName;

    private String userImg;

    private Date createdAt;
}
