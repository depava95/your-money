package com.biedin.core.decorator;

import com.biedin.core.dao.interfaces.StorageDAO;
import com.biedin.core.exceptions.AmountException;
import com.biedin.core.exceptions.CurrencyException;
import com.biedin.core.interfaces.Storage;
import com.biedin.core.utils.TreeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageSync implements StorageDAO {

    private TreeUtils<Storage> treeUtils = new TreeUtils();

    private List<Storage> treeList = new ArrayList<>();
    private Map<Integer, Storage> identityMap = new HashMap<>();
    private StorageDAO storageDAO;

    public StorageSync(StorageDAO storageDAO) {
        this.storageDAO = storageDAO;
        init();
    }

    public void init() {
        List<Storage> storageList = storageDAO.getAll();

        for (Storage s : storageList) {
            identityMap.put(s.getId(), s);
            treeUtils.addToTree(s.getParentId(), s, treeList);
        }

    }


    @Override
    public List<Storage> getAll() {// возвращает объекты уже в виде деревьев
        return treeList;
    }

    @Override
    public Storage get(int id) {// не делаем запрос в БД, а получаем ранее загруженный объект из коллекции
        return identityMap.get(id);
    }


    @Override
    public boolean update(Storage storage) {
        if (storageDAO.update(storage)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Storage storage) {
        if (storageDAO.delete(storage)) {
            removeFromCollections(storage);

            return true;
        }
        return false;
    }


    private void addToCollections(Storage storage) {
        identityMap.put(storage.getId(), storage);

        if (storage.hasParent()) {
            if (!storage.getParent().getChilds().contains(storage)) {
                storage.getParent().addChild(storage);
            }
        } else {
            treeList.add(storage);
        }
    }


    private void removeFromCollections(Storage storage) {
        identityMap.remove(storage.getId());
        if (storage.getParent() != null) {
            storage.getParent().removeChild(storage);
        } else {
            treeList.remove(storage);
        }
    }

    @Override
    public boolean add(Storage storage) {

        if (storageDAO.add(storage)) {
            addToCollections(storage);
            return true;
        }
        return false;
    }

    public StorageDAO getStorageDAO() {
        return storageDAO;
    }

    @Override
    public boolean addCurrency(Storage storage, Currency currency, BigDecimal initAmount) throws CurrencyException {
        if (storageDAO.addCurrency(storage, currency, initAmount)) {
            storage.addCurrency(currency, initAmount);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteCurrency(Storage storage, Currency currency) throws CurrencyException {
        if (storageDAO.deleteCurrency(storage, currency)) {
            storage.deleteCurrency(currency);
            return true;
        }

        return false;
    }

    @Override
    public boolean updateAmount(Storage storage, Currency currency, BigDecimal amount) throws AmountException, CurrencyException {
        if (storageDAO.updateAmount(storage, currency, amount)) {
            try {
                storage.updateAmount(amount, currency);
            } catch (CurrencyException | AmountException e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    public Map<Integer, Storage> getIdentityMap() {
        return identityMap;
    }
}
