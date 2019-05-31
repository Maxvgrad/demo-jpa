package ru.demo.soccer.dao;

import java.io.Serializable;
import java.util.Optional;

public interface GenericDao<T, ID extends Serializable> {

    T create(T t);
    Optional<T> findById(ID id);
    T update(T t);
    void delete(T t);
}
