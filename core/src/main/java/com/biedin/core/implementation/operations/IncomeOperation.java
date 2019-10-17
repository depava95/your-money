package com.biedin.core.implementation.operations;

import com.biedin.core.abstracts.AbstractOperation;
import com.biedin.core.enums.OperationType;
import com.biedin.core.interfaces.Source;
import com.biedin.core.interfaces.Storage;

import java.math.BigDecimal;
import java.util.Currency;

public class IncomeOperation extends AbstractOperation {

    private Source fromSource;
    private Storage toStorage;
    private Currency currency;
    private BigDecimal amount;

    public IncomeOperation() {
        super(OperationType.INCOME);
    }

    public Source getFromSource() {
        return fromSource;
    }

    public void setFromSource(Source fromSource) {
        this.fromSource = fromSource;
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
