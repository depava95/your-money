package com.biedin.core.decorator;

import com.biedin.core.dao.interfaces.OperationDAO;
import com.biedin.core.enums.OperationType;
import com.biedin.core.exceptions.AmountException;
import com.biedin.core.exceptions.CurrencyException;
import com.biedin.core.implementation.operations.ConvertOperation;
import com.biedin.core.implementation.operations.IncomeOperation;
import com.biedin.core.implementation.operations.OutcomeOperation;
import com.biedin.core.implementation.operations.TransferOperation;
import com.biedin.core.interfaces.Operation;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OperationSync implements OperationDAO {

    private OperationDAO operationDAO;

    private List<Operation> operationList;
    private Map<OperationType, List<Operation>> operationMap = new EnumMap<>(OperationType.class);
    private Map<Integer, Operation> identityMap = new HashMap<>();

    private SourceSync sourceSync;
    private StorageSync storageSync;


    public OperationSync(OperationDAO operationDAO, SourceSync sourceSync, StorageSync storageSync) {
        this.operationDAO = operationDAO;
        this.sourceSync = sourceSync;
        this.storageSync = storageSync;
        init();
    }


    public void init() {
        operationList = operationDAO.getAll();
        for (Operation s : operationList) {
            identityMap.put(s.getId(), s);
        }
        fillOperationMap();
    }

    private void fillOperationMap() {
        for (OperationType type : OperationType.values()) {
            operationMap.put(type, operationList.stream().filter(o -> o.getOperationType() == type).collect(Collectors.toList()));
        }
    }

    @Override
    public List<Operation> getAll() {
        Collections.sort(operationList);
        return operationList;
    }

    @Override
    public Operation get(int id) {
        return identityMap.get(id);
    }


    @Override
    public boolean update(Operation operation) {
        if (delete(operationDAO.get(operation.getId()))
                && add(operation)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Operation operation) {
        if (operationDAO.delete(operation) && revertBalance(operation)) {
            removeFromCollections(operation);
            return true;
        }
        return false;
    }

    private boolean revertBalance(Operation operation) {
        boolean updateAmountResult = false;
        try {
            switch (operation.getOperationType()) {
                case INCOME: {

                    IncomeOperation incomeOperation = (IncomeOperation) operation;

                    BigDecimal currentAmount = incomeOperation.getToStorage().getAmount(incomeOperation.getCurrency());
                    BigDecimal newAmount = currentAmount.subtract(incomeOperation.getAmount());

                    updateAmountResult = storageSync.updateAmount(incomeOperation.getToStorage(), incomeOperation.getCurrency(), newAmount);
                    break;
                }
                case OUTCOME: {
                    OutcomeOperation outcomeOperation = (OutcomeOperation) operation;
                    BigDecimal currentAmount = outcomeOperation.getFromStorage().getAmount(outcomeOperation.getCurrency());
                    BigDecimal newAmount = currentAmount.add(outcomeOperation.getAmount());

                    updateAmountResult = storageSync.updateAmount(outcomeOperation.getFromStorage(), outcomeOperation.getCurrency(), newAmount);
                    break;
                }
                case TRANSFER: {
                    TransferOperation trasnferOperation = (TransferOperation) operation;
                    BigDecimal currentAmountFromStorage = trasnferOperation.getFromStorage().getAmount(trasnferOperation.getCurrency());// получаем текущее значение остатка (баланса)
                    BigDecimal newAmountFromStorage = currentAmountFromStorage.add(trasnferOperation.getAmount()); //отнимаем сумму операции
                    BigDecimal currentAmountToStorage = trasnferOperation.getToStorage().getAmount(trasnferOperation.getCurrency());// получаем текущее значение остатка (баланса)
                    BigDecimal newAmountToStorage = currentAmountToStorage.subtract(trasnferOperation.getAmount()); //прибавляем сумму операции
                    updateAmountResult = storageSync.updateAmount(trasnferOperation.getFromStorage(), trasnferOperation.getCurrency(), newAmountFromStorage) &&
                            storageSync.updateAmount(trasnferOperation.getToStorage(), trasnferOperation.getCurrency(), newAmountToStorage);// для успешного результата - оба обновления должны вернуть true
                    break;
                }
                case CONVERT: {
                    ConvertOperation convertOperation = (ConvertOperation) operation;
                    BigDecimal currentAmountFromStorage = convertOperation.getFromStorage().getAmount(convertOperation.getFromCurrency());// получаем текущее значение остатка (баланса)
                    BigDecimal newAmountFromStorage = currentAmountFromStorage.add(convertOperation.getFromAmount()); // сколько отнимаем
                    BigDecimal currentAmountToStorage = convertOperation.getToStorage().getAmount(convertOperation.getToCurrency());// получаем текущее значение остатка (баланса)
                    BigDecimal newAmountToStorage = currentAmountToStorage.subtract(convertOperation.getToAmount()); // сколько прибавляем
                    updateAmountResult = storageSync.updateAmount(convertOperation.getFromStorage(), convertOperation.getFromCurrency(), newAmountFromStorage) &&
                            storageSync.updateAmount(convertOperation.getToStorage(), convertOperation.getToCurrency(), newAmountToStorage);// для успешного результата - оба обновления должны вернуть true
                    break;
                }
            }
        } catch (CurrencyException | AmountException e) {
            e.printStackTrace();
        }
        if (!updateAmountResult) {
            delete(operation);
            return false;
        }
        return true;
    }

    private void removeFromCollections(Operation operation) {
        operationList.remove(operation);
        identityMap.remove(operation.getId());
        operationDAO.getList(operation.getOperationType()).remove(operation);
    }

    @Override
    public boolean add(Operation operation) {
        if (operationDAO.add(operation)) {
            addToCollections(operation);

            if (updateBalance(operation)) {
                return true;
            }

        }
        return false;
    }

    private boolean updateBalance(Operation operation) {
        boolean updateAmountResult = false;
        try {
            switch (operation.getOperationType()) {
                case INCOME: {
                    IncomeOperation incomeOperation = (IncomeOperation) operation;
                    BigDecimal currentAmount = incomeOperation.getToStorage().getAmount(incomeOperation.getCurrency());
                    BigDecimal newAmount = currentAmount.add(incomeOperation.getAmount());
                    updateAmountResult = storageSync.updateAmount(incomeOperation.getToStorage(), incomeOperation.getCurrency(), newAmount);
                    break;
                }
                case OUTCOME: {
                    OutcomeOperation outcomeOperation = (OutcomeOperation) operation;
                    BigDecimal currentAmount = outcomeOperation.getFromStorage().getAmount(outcomeOperation.getCurrency());
                    BigDecimal newAmount = currentAmount.subtract(outcomeOperation.getAmount());
                    updateAmountResult = storageSync.updateAmount(outcomeOperation.getFromStorage(), outcomeOperation.getCurrency(), newAmount);
                    break;
                }
                case TRANSFER: {
                    TransferOperation trasnferOperation = (TransferOperation) operation;
                    BigDecimal currentAmountFromStorage = trasnferOperation.getFromStorage().getAmount(trasnferOperation.getCurrency());
                    BigDecimal newAmountFromStorage = currentAmountFromStorage.subtract(trasnferOperation.getAmount());
                    BigDecimal currentAmountToStorage = trasnferOperation.getToStorage().getAmount(trasnferOperation.getCurrency());
                    BigDecimal newAmountToStorage = currentAmountToStorage.add(trasnferOperation.getAmount());
                    updateAmountResult = storageSync.updateAmount(trasnferOperation.getFromStorage(), trasnferOperation.getCurrency(), newAmountFromStorage) &&
                            storageSync.updateAmount(trasnferOperation.getToStorage(), trasnferOperation.getCurrency(), newAmountToStorage);
                    break;
                }
                case CONVERT: {
                    ConvertOperation convertOperation = (ConvertOperation) operation;
                    BigDecimal currentAmountFromStorage = convertOperation.getFromStorage().getAmount(convertOperation.getFromCurrency());
                    BigDecimal newAmountFromStorage = currentAmountFromStorage.subtract(convertOperation.getFromAmount()); // сколько отнимаем
                    BigDecimal currentAmountToStorage = convertOperation.getToStorage().getAmount(convertOperation.getToCurrency());
                    BigDecimal newAmountToStorage = currentAmountToStorage.add(convertOperation.getToAmount()); // сколько прибавляем


                    // обновляем баланс в обоих хранилищах
                    updateAmountResult = storageSync.updateAmount(convertOperation.getFromStorage(), convertOperation.getFromCurrency(), newAmountFromStorage) &&
                            storageSync.updateAmount(convertOperation.getToStorage(), convertOperation.getToCurrency(), newAmountToStorage);
                    break;
                }
            }
        } catch (CurrencyException | AmountException e) {
            e.printStackTrace();
        }
        if (!updateAmountResult) {
            delete(operation);
            return false;
        }
        return true;
    }

    private void addToCollections(Operation operation) {
        operationList.add(operation);
        identityMap.put(operation.getId(), operation);
        operationMap.get(operation.getOperationType()).add(operation);
    }

    @Override
    public List<Operation> getList(OperationType operationType) {
        return operationMap.get(operationType);
    }


}
