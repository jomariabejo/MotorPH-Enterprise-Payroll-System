package com.jomariabejo.motorph.repository;

import java.io.Serializable;
import java.util.List;

public interface _GenericRepository<T, ID extends Serializable> {

    T findById(ID id);

    List<T> findAll();

    void save(T entity);

    void update(T entity);

    void delete(T entity);
}
