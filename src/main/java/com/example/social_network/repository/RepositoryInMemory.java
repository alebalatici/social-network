package com.example.social_network.repository;

import com.example.social_network.domain.Entity;

import java.util.HashMap;
import java.util.Map;

public class RepositoryInMemory<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected Map<ID, E> entities;

    public RepositoryInMemory() {
        this.entities = new HashMap<>();
    }

    @Override
    public E save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity can't be null");
        }

        if (entities.containsKey(entity.getId())) {
            throw new RepositoryException("an entity with the id " + entity.getId() + " already exists");
        }

        entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E find(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }

        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }

        E removed = entities.remove(id);

        if (removed == null) {
            throw new RepositoryException("entity with id " + id + " not found");
        }

        return removed;
    }

    @Override
    public E update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity can't be null");
        }

        if (!entities.containsKey(entity.getId())) {
            throw new RepositoryException("entity with id " + entity.getId() + " not found");
        }

        entities.put(entity.getId(), entity);
        return null;
    }
}
