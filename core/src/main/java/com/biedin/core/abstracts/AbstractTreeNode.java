package com.biedin.core.abstracts;

import com.biedin.core.interfaces.CompositeTree;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTreeNode implements CompositeTree {

    private int id = -1;
    private List<CompositeTree> childs = new ArrayList<>();
    private CompositeTree parent;
    private String name;
    private int parentId;

    public AbstractTreeNode() {
    }

    public AbstractTreeNode(String name) {
        this.name = name;
    }

    public AbstractTreeNode(List<CompositeTree> childs) {
        this.childs = childs;
    }

    public AbstractTreeNode(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public AbstractTreeNode(int id, List<CompositeTree> childs, CompositeTree parent, String name) {
        this.id = id;
        this.childs = childs;
        this.parent = parent;
        this.name = name;
    }


    @Override
    public void addChild(CompositeTree child) {
        child.setParent(this);
        childs.add(child);
    }

    @Override
    public void setParent(CompositeTree parent) {
        this.parent = parent;
    }

    @Override
    public CompositeTree getParent() {
        return parent;
    }

    @Override
    public void removeChild(CompositeTree child) {
        childs.remove(child);
    }

    @Override
    public List<CompositeTree> getChilds() {
        return childs;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public CompositeTree getChild(int id) {

        for (CompositeTree child : childs) {
            if (child.getId() == id) {
                return child;
            }
        }
        return null;
    }


    @Override
    public boolean hasChilds() {
        return !childs.isEmpty();
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractTreeNode that = (AbstractTreeNode) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
