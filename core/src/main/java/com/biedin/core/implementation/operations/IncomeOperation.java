package com.biedin.core.implementation.operations;

import com.biedin.core.abstracts.AbstractOperation;
import com.biedin.core.interfaces.Source;
import com.biedin.core.interfaces.Storage;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
//TODO Create constructors without id
public class IncomeOperation extends AbstractOperation {

    private Source fromSource;
    private Storage toStorage;
    private Currency currency;
    private BigDecimal amount;

    public IncomeOperation(Source fromSource, Storage toStorage, Currency currency, BigDecimal amount) {
        this.fromSource = fromSource;
        this.toStorage = toStorage;
        this.currency = currency;
        this.amount = amount;
    }

    public IncomeOperation(int id, Calendar dateTime, String extraInfo, Source fromSource, Storage toStorage, Currency currency, BigDecimal amount) {
        super(id, dateTime, extraInfo);
        this.fromSource = fromSource;
        this.toStorage = toStorage;
        this.currency = currency;
        this.amount = amount;
    }

    public IncomeOperation(int id, Source fromSource, Storage toStorage, Currency currency, BigDecimal amount) {
        super(id);
        this.fromSource = fromSource;
        this.toStorage = toStorage;
        this.currency = currency;
        this.amount = amount;
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
