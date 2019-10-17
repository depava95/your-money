package com.biedin.core.implementation;

import com.biedin.core.abstracts.AbstractTreeNode;
import com.biedin.core.exceptions.AmountException;
import com.biedin.core.exceptions.CurrencyException;
import com.biedin.core.interfaces.Storage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultStorage extends AbstractTreeNode implements Storage {

    private Map<Currency, BigDecimal> currencyAmounts = new HashMap<>();
    private List<Currency> currencyList = new ArrayList<>();

    public DefaultStorage() {}

    public DefaultStorage(String name) {
        super(name);
    }

    public DefaultStorage(int id, String name) {
        super(id, name);
    }

    public DefaultStorage(String name, Map<Currency, BigDecimal> currencyAmounts, List<Currency> currencyList) {
        super(name);
        this.currencyAmounts = currencyAmounts;
        this.currencyList = currencyList;
    }

    public DefaultStorage(Map<Currency, BigDecimal> currencyAmounts) {
        this.currencyAmounts = currencyAmounts;
    }

    public DefaultStorage(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }


    @Override
    public Map<Currency, BigDecimal> getCurrencyAmounts() {
        return currencyAmounts;
    }

    public void setCurrencyAmounts(Map<Currency, BigDecimal> currencyAmounts) {
        this.currencyAmounts = currencyAmounts;
    }

    @Override
    public BigDecimal getAmount(Currency currency) throws CurrencyException {
        checkCurrencyExist(currency);
        return currencyAmounts.get(currency);
    }

    @Override
    public void updateAmount(BigDecimal amount, Currency currency) throws CurrencyException, AmountException {
        checkCurrencyExist(currency);
        checkAmount(amount);
        currencyAmounts.put(currency, amount);
    }

    private void checkCurrencyExist(Currency currency) throws CurrencyException {
        if (!currencyAmounts.containsKey(currency)) {
            throw new CurrencyException("Currency ".concat(currency.toString()).concat(" not exist!"));
        }
    }

    private void checkAmount(BigDecimal amount) throws AmountException {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new AmountException("Amount can't be negative");
        }
    }

    @Override
    public void addCurrency(Currency currency, BigDecimal initAmount) throws CurrencyException {
        if (currencyList.contains(currency)) {
            throw new CurrencyException("Currency already exist");
        }
        currencyList.add(currency);
        currencyAmounts.put(currency, initAmount);
    }

    @Override
    public void deleteCurrency(Currency currency) throws CurrencyException {
        checkCurrencyExist(currency);
        if (!currencyAmounts.get(currency).equals(BigDecimal.ZERO)) {
            throw new CurrencyException("Can't delete currency with amount");
        }
        currencyList.remove(currency);
        currencyAmounts.remove(currency);
    }

    @Override
    public List<Currency> getAvailableCurrencies() {
        return currencyList;
    }

    @Override
    public BigDecimal getApproximatelyAmount(Currency currency) {
        //TODO API Привата
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Currency getCurrency(String currencyCode) throws CurrencyException {
        for (Currency currency : currencyList) {
            if (currency.getCurrencyCode().equals(currencyCode)) {
                return currency;
            }
        }

        throw new CurrencyException("Currency (code=" + currencyCode + ") not exist in storage");

    }
}
