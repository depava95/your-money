package com.biedin.core.abstracts;

import com.biedin.core.exceptions.CompositeObjectNotExist;
import com.biedin.core.interfaces.CompositeTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractCompositeTree implements CompositeTree {

    private int id;
    private List<CompositeTree> childs = new ArrayList<>();
    private CompositeTree parent;
    private String name;

    public AbstractCompositeTree() {}

    public AbstractCompositeTree(int id, List<CompositeTree> childs, CompositeTree parent, String name) {
        this.id = id;
        this.childs = childs;
        this.parent = parent;
        this.name = name;
    }

    public AbstractCompositeTree(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public AbstractCompositeTree(String name) {
        this.name = name;
    }

    @Override
    public void setId(int id) {
        if (id < 0) {
            throw  new IllegalArgumentException("ID can't be negative");
        } else {
            this.id = id;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addChild(CompositeTree child) {
        child.setParent(this);
        childs.add(child);
    }

    @Override
    public void removeChild(CompositeTree child) {
        childs.remove(child);
    }

    @Override
    public CompositeTree getChild(int id) throws CompositeObjectNotExist {
        for (CompositeTree child : childs) {
            if (child.getId() == id) {
                return child;
            }
        }
        throw  new CompositeObjectNotExist("Child doesn't exist");
    }

    @Override
    public List<CompositeTree> getChilds() {
        return childs;
    }

    @Override
    public CompositeTree getParent() {
        return parent;
    }

    @Override
    public void setParent(CompositeTree parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCompositeTree that = (AbstractCompositeTree) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
