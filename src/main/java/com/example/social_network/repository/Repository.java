package com.example.social_network.repository;

import com.example.social_network.domain.Entity;

public interface Repository<ID, E extends Entity<ID>> {
    E save(E entity);
    E find(ID id);
    Iterable<E> findAll();
    E delete(ID id);
    E update(E entity);
}
