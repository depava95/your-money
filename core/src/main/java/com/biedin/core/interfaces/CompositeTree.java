package com.biedin.core.interfaces;

import com.biedin.core.exceptions.CompositeObjectNotExist;

import java.util.List;

public interface CompositeTree {

    String getName();
    
    int getId();

    void addChild(CompositeTree child);

    void removeChild(CompositeTree child);

    CompositeTree getChild(int id) throws CompositeObjectNotExist;

    List<CompositeTree> getChilds();

    CompositeTree getParent();

    void setParent(CompositeTree parent);

    boolean hasChilds();

}
