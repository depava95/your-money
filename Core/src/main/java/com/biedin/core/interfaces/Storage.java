package com.biedin.core.interfaces;

import com.biedin.core.exceptions.AmountException;
import com.biedin.core.exceptions.CurrencyException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public interface Storage {

    //getters
    String getName();

    BigDecimal getApproximatelyAmount(Currency currency);

    BigDecimal getAmount(Currency currency) throws CurrencyException;

    Map<Currency, BigDecimal> getCurrencyAmounts();

    //work with currencies
    void addCurrency(Currency currency) throws CurrencyException;

    void deleteCurrency(Currency currency) throws CurrencyException;

    Currency getCurrency(String currencyCode);

    List<Currency> getAvailableCurrencies();

    //work with balances
    void addAmount(BigDecimal amount, Currency currency) throws CurrencyException;

    void expenseAmount(BigDecimal amount, Currency currency) throws CurrencyException, AmountException;

    void changeAmount(BigDecimal amount, Currency currency) throws CurrencyException;

}
