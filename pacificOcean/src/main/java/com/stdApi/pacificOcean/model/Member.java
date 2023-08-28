package com.stdApi.pacificOcean.model;
import lombok.*;

import javax.persistence.*;

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

    private String userRole;

    private String createAt;

    private String updateAt;

    @Builder
    public Member(Long userNo, String userEmail, String password, String userName, String userPhn, String userRole, String createAt, String updateAt) {
        this.userNo = userNo;
        this.userEmail = userEmail;
        this.password = password;
        this.userName = userName;
        this.userPhn = userPhn;
        this.userRole = userRole;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
