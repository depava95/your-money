package com.biedin.core.implementation.operations;

import com.biedin.core.abstracts.AbstractOperation;
import com.biedin.core.interfaces.Storage;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;

public class TransferOperation extends AbstractOperation {

    private Storage fromStorage;
    private Storage toStorage;
    private Currency currency;
    private BigDecimal amount;

    public TransferOperation(Storage fromStorage, Storage toStorage, Currency currency, BigDecimal amount) {
        this.fromStorage = fromStorage;
        this.toStorage = toStorage;
        this.currency = currency;
        this.amount = amount;
    }

    public TransferOperation(int id, Calendar dateTime, String extraInfo, Storage fromStorage, Storage toStorage, Currency currency, BigDecimal amount) {
        super(id, dateTime, extraInfo);
        this.fromStorage = fromStorage;
        this.toStorage = toStorage;
        this.currency = currency;
        this.amount = amount;
    }

    public TransferOperation(int id, Storage fromStorage, Storage toStorage, Currency currency, BigDecimal amount) {
        super(id);
        this.fromStorage = fromStorage;
        this.toStorage = toStorage;
        this.currency = currency;
        this.amount = amount;
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
