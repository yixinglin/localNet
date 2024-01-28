package org.hsgt.order.BO;

import lombok.Data;

import java.io.Serializable;

@Data
public class BankAccount implements Serializable {
    String accountHolder;
    String IBAN;
    String BIC;
    String financialInstitution;
}
