package de.starwit.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * AbstractService used as template for all service implementations and
 * implements the basic
 * functionality (create, read, update, delete, and other basic stuff).
 * 
 * @author Anett
 *
 * @param <E>
 */
public interface ServiceInterface<E, R extends JpaRepository<E, Long>> {

    static Logger LOG = LoggerFactory.getLogger(ServiceInterface.class);

    public R getRepository();

    public default List<E> findAll() {
        return this.getRepository().findAll();
    }

    public default E findById(Long id) {
        return this.getRepository().findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
    }

    public default E saveOrUpdate(E entity) {
        entity = this.getRepository().save(entity);
        return entity;
    }

    public default void delete(Long id) {
        this.getRepository().deleteById(id);
    }

}
