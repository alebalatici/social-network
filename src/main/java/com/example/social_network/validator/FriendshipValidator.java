package com.example.social_network.validator;

import com.example.social_network.domain.Friendship;
import org.springframework.stereotype.Component;

@Component
public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity == null) {
            throw new ValidationException("Friendship can't be null");
        }

        if (entity.getIdUser1() == null || entity.getIdUser2() == null) {
            throw new ValidationException("Friendships can't be between null id's");
        }

        if (entity.getIdUser1().equals(entity.getIdUser2())) {
            throw new ValidationException("Friendships can't be the same id's");
        }
    }
}