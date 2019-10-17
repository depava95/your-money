package com.biedin.core.abstracts;

import com.biedin.core.enums.OperationType;
import com.biedin.core.interfaces.Operation;

import java.util.Calendar;

public abstract class AbstractOperation implements Operation {

    private int id;
    private Calendar dateTime;
    private String description;
    private OperationType operationType;

    public AbstractOperation(OperationType operationType) {
        this.operationType = operationType;
    }

    public AbstractOperation(int id, Calendar dateTime, String description) {
        this.id = id;
        this.description = description;
        this.dateTime = dateTime;
    }

    public AbstractOperation(int id, OperationType operationType) {
        this.id = id;
        this.operationType = operationType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public int compareTo(Operation operation) {
        return operation.getDateTime().compareTo(dateTime);
    }

}
