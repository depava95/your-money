package com.biedin.core.dao.interfaces;

import com.biedin.core.interfaces.Storage;

import java.math.BigDecimal;
import java.util.Currency;

public interface StorageDAO extends CommonDAO {

    boolean addCurrency(Storage storage, Currency currency);

    boolean deleteCurrency(Storage storage, Currency currency);

    boolean updateAmount(Storage storage, BigDecimal amount);

}
