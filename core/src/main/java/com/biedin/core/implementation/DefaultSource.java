package com.biedin.core.implementation;

import com.biedin.core.abstracts.AbstractCompositeTree;
import com.biedin.core.interfaces.CompositeTree;
import com.biedin.core.interfaces.Source;

import java.util.List;

public class DefaultSource extends AbstractCompositeTree implements Source {

    OperationType operationType;

    public DefaultSource() {
    }

    public DefaultSource(int id, List<CompositeTree> childs, CompositeTree parent, String name) {
        super(id, childs, parent, name);
    }

    public DefaultSource(int id, String name) {
        super(id, name);
    }

    public DefaultSource(String name) {
        super(name);
    }

    public DefaultSource(String name, OperationType operationType) {
        super(name);
        this.operationType = operationType;
    }

    @Override
    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public void addChild(CompositeTree child) {
        if (child instanceof DefaultSource) {
            ((DefaultSource) child).setOperationType(operationType);
        }
        super.addChild(child);
    }
}
