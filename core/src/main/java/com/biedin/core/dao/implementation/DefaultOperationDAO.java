package com.biedin.core.dao.implementation;

import com.biedin.core.abstracts.AbstractOperation;
import com.biedin.core.dao.interfaces.OperationDAO;
import com.biedin.core.database.SQLiteConnection;
import com.biedin.core.enums.OperationType;
import com.biedin.core.implementation.operations.ConvertOperation;
import com.biedin.core.implementation.operations.IncomeOperation;
import com.biedin.core.implementation.operations.OutcomeOperation;
import com.biedin.core.implementation.operations.TransferOperation;
import com.biedin.core.interfaces.Operation;
import com.biedin.core.interfaces.Source;
import com.biedin.core.interfaces.Storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultOperationDAO implements OperationDAO {

    private static final String OPERATION_TABLE = "operation";
    private List<Operation> operationList = new ArrayList<>();
    private Map<Integer, Source> sourceIdentityMap;
    private Map<Integer, Storage> storageIdentityMap;

    public DefaultOperationDAO(Map<Integer, Source> sourceIdentityMap, Map<Integer, Storage> storageIdentityMap) {// IdentityMap - распространенное понятие, это коллекция, где вместо ключа id, а значение - это сам объект
        this.sourceIdentityMap = sourceIdentityMap;
        this.storageIdentityMap = storageIdentityMap;
    }

    @Override
    public List<Operation> getAll() {
        operationList.clear();
        try (Statement stmt = SQLiteConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("select * from " + OPERATION_TABLE);) {
            while (rs.next()) {
                operationList.add(fillOperation(rs));
            }
            return operationList;
        } catch (SQLException e) {
            Logger.getLogger(DefaultOperationDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }


    @Override
    public Operation get(int id) {
        try (PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement("select * from " + OPERATION_TABLE + " where id=?");) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery();) {
                if (rs.next()) {
                    AbstractOperation operation = fillOperation(rs);
                    return operation;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(DefaultSourceDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    private AbstractOperation fillOperation(ResultSet rs) throws SQLException {
        OperationType operationType = OperationType.getType(rs.getInt("type_id"));
        AbstractOperation operation = createOperation(operationType, rs);
        operation.setId(rs.getInt("id"));
        operation.setOperationType(operationType);
        Calendar datetime = Calendar.getInstance();
        datetime.setTimeInMillis(rs.getLong("datetime"));
        operation.setDateTime(datetime);
        operation.setDescription(rs.getString("description"));
        return operation;
    }

    private AbstractOperation createOperation(OperationType operationType, ResultSet rs) throws SQLException {
        switch (operationType) {
            case INCOME: {
                IncomeOperation operation = new IncomeOperation();
                operation.setFromSource(sourceIdentityMap.get(rs.getLong("from_source_id")));
                operation.setCurrency(Currency.getInstance(rs.getString("from_currency_code")));
                operation.setAmount(rs.getBigDecimal("from_amount"));
                operation.setToStorage(storageIdentityMap.get(rs.getLong("to_storage_id")));
                return operation;
            }
            case OUTCOME: { // расход
                OutcomeOperation operation = new OutcomeOperation();
                operation.setFromStorage(storageIdentityMap.get(rs.getLong("from_storage_id")));
                operation.setCurrency(Currency.getInstance(rs.getString("from_currency_code")));
                operation.setAmount(rs.getBigDecimal("from_amount"));
                operation.setToSource(sourceIdentityMap.get(rs.getLong("to_source_id")));
                return operation;
            }
            case TRANSFER: {
                TransferOperation operation = new TransferOperation();
                operation.setFromStorage(storageIdentityMap.get(rs.getLong("from_storage_id")));
                operation.setCurrency(Currency.getInstance(rs.getString("from_currency_code")));
                operation.setAmount(rs.getBigDecimal("from_amount"));
                operation.setToStorage(storageIdentityMap.get(rs.getLong("to_storage_id")));
                return operation;
            }
            case CONVERT: {
                ConvertOperation operation = new ConvertOperation();
                operation.setFromStorage(storageIdentityMap.get(rs.getLong("from_storage_id")));
                operation.setFromCurrency(Currency.getInstance(rs.getString("from_currency_code")));
                operation.setFromAmount(rs.getBigDecimal("from_amount"));
                operation.setToStorage(storageIdentityMap.get(rs.getLong("to_storage_id")));
                operation.setToCurrency((Currency.getInstance(rs.getString("to_currency_code"))));
                operation.setToAmount(rs.getBigDecimal("to_amount"));
                return operation;
            }
        }
        return null;

    }

    @Override
    public boolean update(Operation operation) {
        return (delete(operation) && add(operation));// при обновлении - удаляем старую операцию, добавляем новую, т.к. могут поменяться хранилища, источники
    }

    @Override
    public boolean delete(Operation operation) {
        try (PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement("delete from " + OPERATION_TABLE + " where id=?");) {
            stmt.setLong(1, operation.getId());
            if (stmt.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            Logger.getLogger(DefaultSourceDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    @Override
    public boolean add(Operation operation) {
        String sql = createInsertSql(operation); // подготовить sql с нужными параметрами, в зависимости от типа операции
        try (PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setLong(1, operation.getDateTime().getTimeInMillis());
            stmt.setLong(2, operation.getOperationType().getId());
            stmt.setString(3, operation.getDescription());
            switch (operation.getOperationType()) {
                case INCOME:
                    IncomeOperation incomeOperation = (IncomeOperation) operation;
                    stmt.setLong(4, incomeOperation.getFromSource().getId());
                    stmt.setString(5, incomeOperation.getCurrency().getCurrencyCode());
                    stmt.setBigDecimal(6, incomeOperation.getAmount());
                    stmt.setLong(7, incomeOperation.getToStorage().getId());
                    break;

                case OUTCOME:
                    OutcomeOperation outcomeOperation = (OutcomeOperation) operation;

                    stmt.setLong(4, outcomeOperation.getFromStorage().getId());
                    stmt.setString(5, outcomeOperation.getCurrency().getCurrencyCode());
                    stmt.setBigDecimal(6, outcomeOperation.getAmount());
                    stmt.setLong(7, outcomeOperation.getToSource().getId());
                    break;

                case TRANSFER:
                    TransferOperation transferOperation = (TransferOperation) operation;

                    stmt.setLong(4, transferOperation.getFromStorage().getId());
                    stmt.setString(5, transferOperation.getCurrency().getCurrencyCode());
                    stmt.setBigDecimal(6, transferOperation.getAmount());
                    stmt.setLong(7, transferOperation.getToStorage().getId());
                    break;

                case CONVERT:
                    ConvertOperation convertOperation = (ConvertOperation) operation;

                    stmt.setLong(4, convertOperation.getFromStorage().getId());
                    stmt.setString(5, convertOperation.getFromCurrency().getCurrencyCode());
                    stmt.setBigDecimal(6, convertOperation.getFromAmount());
                    stmt.setLong(7, convertOperation.getToStorage().getId());
                    stmt.setString(8, convertOperation.getToCurrency().getCurrencyCode());
                    stmt.setBigDecimal(9, convertOperation.getToAmount());
                    break;
            }


            if (stmt.executeUpdate() == 1) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        operation.setId(rs.getInt(1));
                    }
                    return true;
                }
            }

        } catch (SQLException e) {
            Logger.getLogger(DefaultSourceDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }


    private String createInsertSql(Operation operation) {
        StringBuilder sb = new StringBuilder("insert into " + OPERATION_TABLE + " (datetime, type_id, description, ");// sql собирается частями, в зависимости от типа операции
        switch (operation.getOperationType()) {
            case INCOME:
                return sb.append("from_source_id, from_currency_code, from_amount, to_storage_id) values(?,?,?,?,?,?,?)").toString();
            case OUTCOME:
                return sb.append("from_storage_id, from_currency_code, from_amount, to_source_id) values(?,?,?,?,?,?,?)").toString();
            case TRANSFER:
                return sb.append("from_storage_id, from_currency_code, from_amount, to_storage_id) values(?,?,?,?,?,?,?)").toString();
            case CONVERT:
                return sb.append("from_storage_id, from_currency_code, from_amount, to_storage_id, to_currency_code, to_amount) values(?,?,?,?,?,?,?,?,?)").toString();
        }
        return null;

    }


    @Override
    public List<Operation> getList(OperationType operationType) {
        operationList.clear();
        try (PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement("select * from " + OPERATION_TABLE + " where type_id=?")) {
            stmt.setLong(1, operationType.getId());
            try (ResultSet rs = stmt.executeQuery();) {
                while (rs.next()) {
                    operationList.add(fillOperation(rs));
                }
                return operationList;
            } catch (SQLException e) {
                Logger.getLogger(DefaultOperationDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (SQLException e) {
            Logger.getLogger(DefaultOperationDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
