package ru.demo.soccer.dao;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class JpaGenericDao<T, ID extends Serializable> implements GenericDao<T, ID> {

    protected final Class<T> entityClass;

    @PersistenceContext(unitName = "soccer")
    protected EntityManager entityManager;

    @Transactional
    @Override
    public T create(T t) {
        this.entityManager.persist(t);
        return t;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(this.entityManager.find(entityClass, id));
    }

    @Transactional
    @Override
    public T update(T t) {
        return this.entityManager.merge(t);
    }

    @Transactional
    @Override
    public void delete(T t) {
        t = this.entityManager.merge(t);
        this.entityManager.remove(t);
    }
}