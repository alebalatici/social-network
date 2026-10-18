package com.example.social_network.repository;

import com.example.social_network.domain.User;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class UserRepository extends AbstractJsonRepository<Long, User> {
    public UserRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected List<User> readFromFile(File file) throws IOException {
        return objectMapper.readValue(file, new TypeReference<List<User>>() {
        });
    }
}