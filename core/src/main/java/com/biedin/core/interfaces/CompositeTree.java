package com.biedin.core.interfaces;

import java.util.List;

public interface CompositeTree {

    String getName();
    
    int getId();

    void setId(int id);

    int getParentId();

    void addChild(CompositeTree child);

    void removeChild(CompositeTree child);

    List<CompositeTree> getChilds();

    CompositeTree getChild(int id);

    CompositeTree getParent();

    void setParent(CompositeTree parent);

    boolean hasChilds();

    boolean hasParent();

}
