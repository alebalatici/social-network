package com.example.social_network.repository;

import com.example.social_network.domain.Entity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class AbstractJsonRepository <ID, E extends Entity<ID>> extends RepositoryInMemory<ID, E> {
    private final String fileName;
    protected final ObjectMapper objectMapper;

    public AbstractJsonRepository(String fileName) {
        super();
        this.fileName = fileName;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        loadData();
    }

    private void loadData() {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }

        try {
            List<E> list = readFromFile(file);
            for (E entity : list) {
                super.save(entity);
            }
        }
        catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    private void saveData() {
        try {
            objectMapper.writeValue(new File(fileName), findAll());
        }

        catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    protected abstract List<E> readFromFile(File file) throws IOException;

    @Override
    public E save(E entity) {
        E result = super.save(entity);

        if (result == null) {
            saveData();
        }

        return result;
    }

    @Override
    public E delete(ID id) {
        E result = super.delete(id);

        if (result != null) {
            saveData();
        }

        return result;
    }

    @Override
    public E update(E entity) {
        E result = super.update(entity);

        if (result == null) {
            saveData();
        }

        return result;
    }
}
