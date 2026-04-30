package iss.repository;

import iss.model.Entity;

import java.util.List;
import java.util.Optional;

public interface IRepository<ID, E extends Entity<ID>> {
    E save(E entity);
    E update(E entity);
    boolean delete(E entity);
    List<E> findAll();
    Optional<E> findById(ID id);
}
