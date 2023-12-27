package org.hsgt.order.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class BankAccount implements Serializable {
    String accountHolder;
    String IBAN;
    String BIC;
    String financialInstitution;
}
