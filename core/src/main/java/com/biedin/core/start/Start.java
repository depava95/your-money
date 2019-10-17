package com.biedin.core.start;

import com.biedin.core.dao.implementation.DefaultStorageDAO;
import com.biedin.core.decorator.StorageSync;
import com.biedin.core.exceptions.CurrencyException;
import com.biedin.core.implementation.DefaultStorage;

import java.math.BigDecimal;
import java.util.Currency;

public class Start {
    public static void main(String[] args) {
        StorageSync storageSync = new StorageSync(new DefaultStorageDAO());
        DefaultStorage defaultStorage = (DefaultStorage) storageSync.getAll().get(2);
        try {
            storageSync.addCurrency(defaultStorage, Currency.getInstance("USD"), new BigDecimal(100));
            System.out.println(storageSync.getAll());
        } catch (CurrencyException e) {
            e.printStackTrace();
        }
    }
    //TODO Something changes;
}
