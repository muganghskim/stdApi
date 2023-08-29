package com.stdApi.pacificOcean.model;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    private String userEmail;

    private String password;

    private String userName;

    private String userPhn;

    private String userImg;

    private String userRole;

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
    public Member(Long userNo, String userEmail, String password, String userName, String userImg, String userPhn, String userRole) {
        this.userNo = userNo;
        this.userEmail = userEmail;
        this.password = password;
        this.userName = userName;
        this.userImg = userImg;
        this.userPhn = userPhn;
        this.userRole = userRole;
    }

    public Member update(String userName, String userImg) {
        if (userName != null) {
            this.userName = userName;
        }
        if (userImg != null) {
            this.userImg = userImg;
        }
        return this;
    }
}
