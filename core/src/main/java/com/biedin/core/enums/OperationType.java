package com.biedin.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum OperationType {

    INCOME(1), OUTCOME(2), TRANSFER(3), CONVERT(4);

    private static Map<Integer, OperationType> map = new HashMap<>();

    static {
        for (OperationType type : OperationType.values()) {
            map.put(type.getId(), type);
        }
    }

    private Integer id;

    private OperationType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static OperationType getType(Integer id) {
        return map.get(id);
    }
}
