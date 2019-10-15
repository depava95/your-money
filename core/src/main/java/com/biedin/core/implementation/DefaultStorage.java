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
    private List<Currency> availableCurrencies = new ArrayList<>();

    public DefaultStorage() {
    }

    public DefaultStorage(String name) {
        super(name);
    }

    public DefaultStorage(int id, String name) {
        super(id, name);
    }

    public DefaultStorage(String name, Map<Currency, BigDecimal> currencyAmounts, List<Currency> availableCurrencies) {
        super(name);
        this.currencyAmounts = currencyAmounts;
        this.availableCurrencies = availableCurrencies;
    }

    public DefaultStorage(Map<Currency, BigDecimal> currencyAmounts, List<Currency> availableCurrencies) {
        this.currencyAmounts = currencyAmounts;
        this.availableCurrencies = availableCurrencies;
    }

    public DefaultStorage(Map<Currency, BigDecimal> currencyAmounts) {
        this.currencyAmounts = currencyAmounts;
    }

    public DefaultStorage(List<Currency> availableCurrencies) {
        this.availableCurrencies = availableCurrencies;
    }

    @Override
    public List<Currency> getAvailableCurrencies() {
        return availableCurrencies;
    }

    public void setAvailableCurrencies(List<Currency> availableCurrencies) {
        this.availableCurrencies = availableCurrencies;
    }

    @Override
    public Map<Currency, BigDecimal> getCurrencyAmounts() {
        return currencyAmounts;
    }

    public void setCurrencyAmounts(Map<Currency, BigDecimal> currencyAmounts) {
        this.currencyAmounts = currencyAmounts;
    }

    @Override
    public BigDecimal getApproximatelyAmount(Currency currency) {
        //TODO API Привата
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public BigDecimal getAmount(Currency currency) throws CurrencyException {
        checkCurrencyExist(currency);
        return currencyAmounts.get(currency);
    }

    @Override
    public void addCurrency(Currency currency) throws CurrencyException {
        if (availableCurrencies.contains(currency)) {
            throw new CurrencyException("Currency already exist");
        }
        availableCurrencies.add(currency);
        currencyAmounts.put(currency, BigDecimal.ZERO);
    }

    @Override
    public void deleteCurrency(Currency currency) throws CurrencyException {
        checkCurrencyExist(currency);
        if (!currencyAmounts.get(currency).equals(BigDecimal.ZERO)) {
            throw new CurrencyException("Can't delete currency with amount");
        }
        availableCurrencies.remove(currency);
        currencyAmounts.remove(currency);
    }

    @Override
    public Currency getCurrency(String currencyCode) {
        return Currency.getInstance(currencyCode);
    }

    @Override
    public void addAmount(BigDecimal amount, Currency currency) throws CurrencyException, IllegalArgumentException {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount can't be negative");
        }
        checkCurrencyExist(currency);
        BigDecimal oldAmount = currencyAmounts.get(currency);
        currencyAmounts.put(currency, oldAmount.add(amount));
    }

    @Override
    public void expenseAmount(BigDecimal amount, Currency currency) throws CurrencyException, AmountException, IllegalArgumentException {
        checkCurrencyExist(currency);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount can't be negative");
        }
        BigDecimal oldAmount = currencyAmounts.get(currency);
        if (oldAmount.compareTo(amount) < 0) throw new AmountException("Balance can't be negative");
        currencyAmounts.put(currency, oldAmount.subtract(amount));
    }

    @Override
    public void changeAmount(BigDecimal amount, Currency currency) throws CurrencyException {
        checkCurrencyExist(currency);
        currencyAmounts.put(currency, amount);
    }

    private void checkCurrencyExist(Currency currency) throws CurrencyException {
        if (!currencyAmounts.containsKey(currency)) {
            throw new CurrencyException("Currency ".concat(currency.toString()).concat(" not exist!"));
        }
    }
}
