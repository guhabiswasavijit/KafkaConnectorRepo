package com.example.users;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.io.Serializable;
import java.util.UUID;
@Getter
@EqualsAndHashCode
public class PaytmTransactionKey implements Serializable {
    private String transactionId;
    public void setTransactionId() {
        UUID randomTransId = UUID.randomUUID();
        this.transactionId = randomTransId.toString();
    }
}
