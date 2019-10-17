package com.biedin.core.decorator;

import com.biedin.core.dao.interfaces.SourceDAO;
import com.biedin.core.enums.OperationType;
import com.biedin.core.interfaces.Source;
import com.biedin.core.utils.TreeUtils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SourceSync implements SourceDAO {

    private TreeUtils<Source> treeUtils = new TreeUtils<>();
    private List<Source> treeList = new ArrayList<>();
    private Map<OperationType, List<Source>> sourceMap = new EnumMap<>(OperationType.class);
    private Map<Integer, Source> identityMap = new HashMap<>();
    private SourceDAO sourceDAO;

    public SourceSync(SourceDAO sourceDAO) {
        this.sourceDAO = sourceDAO;
        init();
    }

    private void init() {
        List<Source> sourceList = sourceDAO.getAll();

        for(Source source : sourceList) {
            identityMap.put(source.getId(), source);
            treeUtils.addToTree(source.getParentId(), source, treeList);
        }
        fillSourceMap(treeList);
    }

    private void fillSourceMap(List<Source> treeList) {
        for (OperationType type : OperationType.values()) {
            sourceMap.put(type, treeList.stream().filter(s -> s.getOperationType() == type).collect(Collectors.toList()));
        }
    }

    @Override
    public List<Source> getAll() {
        return treeList;
    }

    @Override
    public Source get(int id) {
        return identityMap.get(id);
    }

    @Override
    public boolean update(Source object) {
       if (sourceDAO.update(object)) {
           return true;
       }
       return false;
    }

    @Override
    public boolean delete(Source source) {
        if (sourceDAO.delete(source)) {
            removeFromCollections(source);
            return true;
        }
        return false;
    }

    @Override
    public boolean add(Source source) {
        if (sourceDAO.add(source)) {
            addToCollections(source);
            return true;
        }
        return false;
    }

    private void addToCollections(Source source) {
        identityMap.put(source.getId(), source);
        if (source.hasParent()) {
            if (!source.getParent().getChilds().contains(source)) {
                source.getParent().addChild(source);
            }
        } else {
            sourceMap.get(source.getOperationType()).add(source);
            treeList.add(source);
        }
    }

    private void removeFromCollections(Source source) {
        identityMap.remove(source.getId());
        if (source.hasParent()) {
            source.getParent().removeChild(source);
        } else {
            sourceMap.get(source.getOperationType()).remove(source);
            treeList.remove(source);
        }
    }

    public List<Source> getList(OperationType operationType) {
        return sourceMap.get(operationType);
    }

    public Map<Integer, Source> getIdentityMap() {
        return identityMap;
    }
}
