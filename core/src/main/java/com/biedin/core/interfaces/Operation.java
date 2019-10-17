package com.biedin.core.interfaces;

import com.biedin.core.enums.OperationType;

import java.util.Calendar;

public interface Operation extends Comparable<Operation> {

    int getId();

    void setId(int id);

    OperationType getOperationType();

    Calendar getDateTime();

    String getDescription();

}
