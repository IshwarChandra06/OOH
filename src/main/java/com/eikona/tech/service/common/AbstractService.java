package com.eikona.tech.service.common;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

@SuppressWarnings("hiding")
public interface AbstractService<T, Long> {

    T find(final Long id);

    List<T> findAll();

    T save(final T entity);

    T update(final T entity);

    void delete(final Long entityId);

}
