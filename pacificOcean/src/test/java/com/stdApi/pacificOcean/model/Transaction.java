package com.stdApi.pacificOcean.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userNo")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdNo")
    private Product product;

    private String rcvName;

    private String rcvPhn;

    private String tidStat;

    private String createAt;

    private String updateAt;

    @Builder
    public Transaction(Long tid, Member member, Product product, String orderDttm, String address1, String address2, String address3, String rcvName, String rcvPhn, String tidStat, String createAt, String updateAt) {
        this.tid = tid;
        this.member = member;
        this.product = product;
        this.rcvName = rcvName;
        this.rcvPhn = rcvPhn;
        this.tidStat = tidStat;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}