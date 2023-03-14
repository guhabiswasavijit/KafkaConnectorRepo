package com.example.users;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;
@Setter
@Getter
@EqualsAndHashCode
public class PaytmTransaction implements Serializable {
    private Date transactionDate;
    private String activity;
    private String sourceDestination;
    private String walletTxnID;
    private String comment;
    private Integer debit;
    private Integer credit;
    private String transactionBreakup;
    private String transactionStatus;
}
