package com.example.social_network;

import com.example.social_network.domain.Friendship;
import com.example.social_network.domain.Tuple;
import com.example.social_network.domain.User;
import com.example.social_network.validator.UserValidator;
import com.example.social_network.validator.ValidationException;
import com.example.social_network.validator.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestsDomainValidator {
    @Test
    void testUser() {
        User user = new User("username", "firstName", "lastName", "email@email.com", LocalDate.of(1990, 1, 1));
        user.setId(1L);

        assertEquals(1L, user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("firstName", user.getFirstName());
        assertEquals("lastName", user.getLastName());
        assertEquals("email@email.com", user.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
    }

    @Test
    void testUserEqualsAndHashCode() {
        LocalDate date = LocalDate.of(1990, 1, 1);
        User u1 = new User("username", "firstName", "lastName", "email@email.com", LocalDate.of(1990, 1, 1));
        User u2 = new User("username", "firstName", "lastName", "email@email.com", LocalDate.of(1990, 1, 1));

        u1.setId(1L);
        u2.setId(1L);

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());

        u2.setId(2L);
        assertNotEquals(u1, u2);
    }

    @Test
    void testUserValidator() {
        Validator<User> validator = new UserValidator();
        User u1 = new User("username1", "firstName1", "lastName1", "email1@email.com", LocalDate.of(1990, 1, 1));
        assertDoesNotThrow(()-> validator.validate(u1));

        User u2 = new User("", "firstName2", "lastName2", "email2@email.com", LocalDate.of(1990, 1, 2));
        assertThrows(ValidationException.class, ()-> validator.validate(u2));

        User u3 = new User(null, "firstName3", "lastName3", "email3@email.com", LocalDate.of(1990, 1, 3));
        assertThrows(ValidationException.class, ()-> validator.validate(u3));

        User u4 = new User("username4", "", "lastName4", "email4@email.com", LocalDate.of(1990, 1, 4));
        assertThrows(ValidationException.class, ()-> validator.validate(u4));

        User u5 = new User("username5", null, "lastName5", "email5@email.com", LocalDate.of(1990, 1, 5));
        assertThrows(ValidationException.class, ()-> validator.validate(u5));

        User u6 = new User("username6", "firstName6", "", "email6@email.com", LocalDate.of(1990, 1, 6));
        assertThrows(ValidationException.class, ()-> validator.validate(u6));

        User u7 = new User("username7", "firstName7", null, "email7@email.com", LocalDate.of(1990, 1, 7));
        assertThrows(ValidationException.class, ()-> validator.validate(u7));

        User u8 = new User("username8", "firstName8", "lastName8", null, LocalDate.of(1990, 1, 8));
        assertThrows(ValidationException.class, ()-> validator.validate(u8));

        User u9 = new User("username9", "firstName9", null, "email9emailcom", LocalDate.of(1990, 1, 9));
        assertThrows(ValidationException.class, ()-> validator.validate(u9));

        User u10 = new User("username10", "firstName10", "lastName10", "email10@email.com", null);
        assertThrows(ValidationException.class, ()-> validator.validate(u10));

        User u11 = new User("username11", "firstName11", "lastName11", "email11@email.com", LocalDate.now().plusYears(10));
        assertThrows(ValidationException.class, ()-> validator.validate(u11));

        User u12 = new User("username12", "firstName12", "lastName12", "email12@email.com", LocalDate.now().minusYears(10));
        assertThrows(ValidationException.class, ()-> validator.validate(u12));
    }

    @Test
    void testFriendship() {
        Long idUser1 = 1L;
        Long idUser2 = 2L;
        LocalDateTime now = LocalDateTime.now();

        Friendship friendship = new Friendship(idUser1, idUser2, now);

        assertEquals(idUser1, friendship.getIdUser1());
        assertEquals(idUser2, friendship.getIdUser2());
        assertEquals(now, friendship.getFriendsFrom());

        Tuple<Long, Long> expectedTuple = new Tuple<>(idUser1, idUser2);
        assertEquals(expectedTuple, friendship.getId());

        Friendship f1 = new Friendship(idUser1, idUser2);
        Friendship f2 = new Friendship(idUser2, idUser1);
        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());

        Friendship f3 = new Friendship(idUser1, 3L);
        assertNotEquals(f1, f3);
    }
}