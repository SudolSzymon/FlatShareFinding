package com.szymon.ffproject.dao;

import com.szymon.ffproject.database.entity.Entity;
import java.time.LocalDateTime;

public abstract class DAO<T extends Entity> {

    public final void save(T entity) {
        entity.setModificationTime(LocalDateTime.now());
        executeSave(entity);
    }

    protected abstract void executeSave(T entity);

    public abstract T get(String id);

    public abstract boolean exist(String id);

    public abstract void delete(String id);
}
