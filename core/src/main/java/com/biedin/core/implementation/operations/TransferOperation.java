package com.biedin.core.implementation.operations;

import com.biedin.core.abstracts.AbstractOperation;
import com.biedin.core.enums.OperationType;
import com.biedin.core.interfaces.Storage;

import java.math.BigDecimal;
import java.util.Currency;

public class TransferOperation extends AbstractOperation {

    private Storage fromStorage;
    private Storage toStorage;
    private Currency currency;
    private BigDecimal amount;

    public TransferOperation() {
        super(OperationType.TRANSFER);
    }

    public Storage getFromStorage() {
        return fromStorage;
    }

    public void setFromStorage(Storage fromStorage) {
        this.fromStorage = fromStorage;
    }

    public Storage getToStorage() {
        return toStorage;
    }

    public void setToStorage(Storage toStorage) {
        this.toStorage = toStorage;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
