package com.szymon.ffproject.dao;

public interface GenericDAO<T, U> {

    void save(T object);


    T get(U id);

    boolean exist(U id);

    void delete(U id);
}
