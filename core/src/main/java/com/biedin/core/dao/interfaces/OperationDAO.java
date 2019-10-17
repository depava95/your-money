package com.biedin.core.dao.interfaces;

import com.biedin.core.enums.OperationType;
import com.biedin.core.interfaces.Operation;

import java.util.List;

public interface OperationDAO extends CommonDAO<Operation> {

    List<Operation> getList(OperationType operationType);

}
