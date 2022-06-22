package com.khai.edu.knysh.provide_and_order_services.service.impl;

import com.khai.edu.knysh.provide_and_order_services.service.GenericService;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractService<T> implements GenericService<T> {

    private final CrudRepository<T, Long> repository;

    protected AbstractService(CrudRepository<T, Long> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    protected List<T> getList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        return getList(repository.saveAll(entities));
    }

    @Override
    public Optional<T> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return getList(repository.findAll());
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

}
