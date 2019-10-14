package com.biedin.core.start;

import com.biedin.core.exceptions.CurrencyException;
import com.biedin.core.implementation.DefaultStorage;
import com.biedin.core.interfaces.Storage;

import java.math.BigDecimal;
import java.util.Currency;

public class Start {
    public static void main(String[] args) {
        try {

            Storage storage = new DefaultStorage();

            Currency currencyUSD = Currency.getInstance("USD");
            Currency currencyRUB = Currency.getInstance("RUB");
            BigDecimal bigDecimal = new BigDecimal(100);
            BigDecimal bigDecimal2 = new BigDecimal(101);

            storage.addCurrency(currencyUSD);
            storage.addAmount(bigDecimal, currencyUSD);
            System.out.println(storage.getAmount(currencyUSD));
            System.out.println(storage.getAvailableCurrencies());


        } catch (CurrencyException e) {
            e.printStackTrace();
        }
    }
}
