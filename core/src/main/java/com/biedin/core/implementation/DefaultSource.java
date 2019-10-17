package com.biedin.core.implementation;

import com.biedin.core.abstracts.AbstractTreeNode;
import com.biedin.core.enums.OperationType;
import com.biedin.core.interfaces.CompositeTree;
import com.biedin.core.interfaces.Source;

import java.util.List;

public class DefaultSource extends AbstractTreeNode implements Source {

    OperationType operationType;

    public DefaultSource() {
    }

    public DefaultSource(String name) {
        super(name);
    }

    public DefaultSource(List<CompositeTree> childs) {
        super(childs);
    }

    public DefaultSource(int id, String name) {
        super(id, name);
    }

    public DefaultSource(int id, List<CompositeTree> childs, CompositeTree parent, String name) {
        super(id, childs, parent, name);
    }

    public DefaultSource(int id, String name, OperationType operationType) {
        super(id, name);
        this.operationType = operationType;
    }


    @Override
    public OperationType getOperationType() {
        return operationType;
    }


    public void setOperationType(OperationType operationType) {
        if(!hasParent()) {
            this.operationType = operationType;
        }
    }

    @Override
    public void addChild(CompositeTree child) {
        if (child instanceof DefaultSource) {
            ((DefaultSource) child).setOperationType(operationType);
        }
        super.addChild(child);
    }

    @Override
    public void  setParent(CompositeTree parent) {
        if (parent instanceof DefaultSource) {
            operationType = ((DefaultSource)parent).getOperationType();
        }
        super.setParent(parent);
    }
}
