package com.biedin.core.dao.interfaces;

import com.biedin.core.enums.OperationType;
import com.biedin.core.interfaces.Source;

import java.util.List;

public interface SourceDAO extends CommonDAO<Source> {

    List<Source> getList(OperationType operationType);

}
