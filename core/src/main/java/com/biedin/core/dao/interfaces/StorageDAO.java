package com.biedin.core.dao.interfaces;

import com.biedin.core.exceptions.AmountException;
import com.biedin.core.exceptions.CurrencyException;
import com.biedin.core.interfaces.Storage;

import java.math.BigDecimal;
import java.util.Currency;

public interface StorageDAO extends CommonDAO<Storage> {

    boolean addCurrency(Storage storage, Currency currency, BigDecimal initAmount) throws CurrencyException;

    boolean deleteCurrency(Storage storage, Currency currency) throws CurrencyException;

    boolean updateAmount(Storage storage, Currency currency, BigDecimal amount) throws AmountException, CurrencyException;

}
