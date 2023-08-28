package com.stdApi.pacificOcean.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Delivery")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliverId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userNo")
    private Member member;

    private String userAddress1;

    private String userAddress2;

    private String userAddress3;

    private String createAt;

    private String updateAt;

    @Builder
    public Delivery(Long deliverId, Member member, String userAddress1, String userAddress2, String userAddress3, String createAt, String updateAt) {
        this.deliverId = deliverId;
        this.member = member;
        this.userAddress1 = userAddress1;
        this.userAddress2 = userAddress2;
        this.userAddress3 = userAddress3;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
