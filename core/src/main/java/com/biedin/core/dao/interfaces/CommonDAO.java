package com.biedin.core.dao.interfaces;

import java.util.List;

public interface CommonDAO<T> {

    List<T> getAll();

    T get(int id);

    boolean update(T object);

    boolean delete(T object);

    boolean add(T object);

}
