package com.biedin.core.utils;

import com.biedin.core.interfaces.CompositeTree;

import java.util.List;

public class TreeUtils<T extends CompositeTree> {

    public boolean addToTree(int parentId, T newNode, List<T> list) {
        if (parentId != 0) {
            for (T currentNode : list) {
                if (currentNode.getId() == parentId) {
                    currentNode.addChild(newNode);
                    return true;
                } else {
                    CompositeTree node = recursiveSearch(parentId, currentNode);
                    if (node != null) {
                        node.addChild(newNode);
                        return true;
                    }
                }
            }
        }
        list.add(newNode);
        return false;
    }


    private CompositeTree recursiveSearch(int parentId, CompositeTree child) {
        for (CompositeTree node : child.getChilds()) {
            if (node.getId() == parentId) {
                return node;
            } else if (node.hasChilds()) {
                recursiveSearch(parentId, node);
            }
        }
        return null;
    }
}
