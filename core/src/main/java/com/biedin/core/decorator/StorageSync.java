package com.biedin.core.decorator;

import com.biedin.core.dao.interfaces.StorageDAO;
import com.biedin.core.exceptions.AmountException;
import com.biedin.core.exceptions.CurrencyException;
import com.biedin.core.interfaces.Storage;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class StorageSync implements StorageDAO {

    private StorageDAO storageDAO;
    private List<Storage> storageList;

    public StorageSync (StorageDAO storageDAO) {
        this.storageDAO = storageDAO;
        init();
    }

    private void init() {
        storageList = storageDAO.getAll();
    }

    @Override
    public boolean addCurrency(Storage storage, Currency currency, BigDecimal amount) throws CurrencyException {
        if (storageDAO.addCurrency(storage, currency, amount)) {
            storage.addCurrency(currency, amount);
            return  true;
        }
        return  false;
    }

    @Override
    public boolean deleteCurrency(Storage storage, Currency currency) throws CurrencyException {
        if (storageDAO.deleteCurrency(storage, currency)) {
            storage.deleteCurrency(currency);
            return  true;
        }
        return false;
    }

    @Override
    public boolean updateAmount(Storage storage, Currency currency, BigDecimal amount) throws AmountException, CurrencyException {
        if (storageDAO.updateAmount(storage, currency, amount)) {
            storage.updateAmount(amount, currency);
            return true;
        }
        return false;
    }

    @Override
    public List<Storage> getAll() {
        if (storageList == null) {
            storageList = storageDAO.getAll();
        }
        return storageList;
    }

    @Override
    public Storage get(int id) {
        return storageDAO.get(id);
    }

    @Override
    public boolean update(Storage object) {
        if (storageDAO.update(object)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Storage object) {
        if (storageDAO.delete(object)) {
            storageList.remove(object);
            return true;
        }
        return false;
    }

    @Override
    public boolean add(Storage object) {
        return false;
    }
}
