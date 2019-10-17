package com.biedin.core.interfaces;

import com.biedin.core.exceptions.AmountException;
import com.biedin.core.exceptions.CurrencyException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public interface Storage extends CompositeTree {

    Map<Currency, BigDecimal> getCurrencyAmounts();

    BigDecimal getAmount(Currency currency) throws CurrencyException;

    BigDecimal getApproximatelyAmount(Currency currency);

    void updateAmount(BigDecimal amount, Currency currency) throws CurrencyException, AmountException;

    Currency getCurrency(String currencyCode) throws CurrencyException;

    void addCurrency(Currency currency, BigDecimal initAmount) throws CurrencyException;

    void deleteCurrency(Currency currency) throws CurrencyException;

    List<Currency> getAvailableCurrencies();

}
