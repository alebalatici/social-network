package com.example.social_network.repository;

import com.example.social_network.domain.Friendship;
import com.example.social_network.domain.Tuple;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class FriendshipRepository extends AbstractJsonRepository<Tuple<Long, Long>, Friendship> {
    public FriendshipRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected List<Friendship> readFromFile(File file) throws IOException {
        return objectMapper.readValue(file, new TypeReference<List<Friendship>>() {});
    }
}