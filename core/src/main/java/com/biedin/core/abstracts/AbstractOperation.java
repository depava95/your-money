package com.biedin.core.abstracts;

import java.util.Calendar;

public abstract class AbstractOperation {

    private int id;
    private Calendar dateTime;
    private String extraInfo;

    public AbstractOperation(){}

    public AbstractOperation(int id, Calendar dateTime, String extraInfo) {
        this.id = id;
        this.dateTime = dateTime;
        this.extraInfo = extraInfo;
    }

    public AbstractOperation(int id) {
        this.id = id;
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

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}
