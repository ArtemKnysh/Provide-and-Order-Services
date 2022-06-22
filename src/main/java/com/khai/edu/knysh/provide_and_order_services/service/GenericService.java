package com.khai.edu.knysh.provide_and_order_services.service;

import java.util.List;
import java.util.Optional;

public interface GenericService<T> {

    T save(T entity);

    List<T> saveAll(Iterable<T> entities);

    Optional<T> findById(long id);

    List<T> findAll();

    long count();

    void deleteById(long id);

    void delete(T entity);

}
