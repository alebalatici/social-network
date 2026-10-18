package com.example.social_network;

import com.example.social_network.domain.Friendship;
import com.example.social_network.domain.Tuple;
import com.example.social_network.domain.User;
import com.example.social_network.repository.*;
import com.example.social_network.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class TestsRepo {
    @TempDir
    Path tempDirUsers;

    @TempDir
    Path tempDirFriendships;

    private Repository<Long, User> userRepository;
    private Repository<Long, User> userRepositoryFile;
    private Repository<Tuple<Long, Long>, Friendship> friendshipRepository;
    private Repository<Tuple<Long, Long>, Friendship> friendshipRepositoryFile;

    @BeforeEach
    void setUp() {
        userRepository = new RepositoryInMemory<>();
        friendshipRepository = new RepositoryInMemory<>();

        String tempFilePathUsers = tempDirUsers.resolve("test_users.json").toString();
        FileUtils.copyTargetTemplate(tempFilePathUsers, "/users.json");

        String tempFilePathFriendships = tempDirFriendships.resolve("test_friendships.json").toString();
        FileUtils.copyTargetTemplate(tempFilePathFriendships, "/friendships.json");

        userRepositoryFile = new UserRepository(tempFilePathUsers);
        friendshipRepositoryFile = new FriendshipRepository(tempFilePathFriendships);
    }

    @Test
    void testSaveAndFindMemory() {
        User user1 = new User("username1", "firstName1", "lastName1", "email1@email.com", LocalDate.now());
        user1.setId(1L);
        User user2 = new User("username2", "firstName2", "lastName2", "email2@email.com", LocalDate.now());
        user2.setId(2L);
        User user3 = new User("username3", "firstName3", "lastName3", "email3@email.com", LocalDate.now());
        user3.setId(3L);

        assertNull(userRepository.save(user1));
        assertNull(userRepository.save(user2));
        assertNull(userRepository.save(user3));

        assertEquals(3, StreamSupport.stream(userRepository.findAll().spliterator(), false).count());
        User foundUser1 = userRepository.find(1L);
        assertNotNull(foundUser1);
        assertEquals(user1, foundUser1);

        User foundUser2 = userRepository.find(2L);
        assertNotNull(foundUser2);
        assertEquals(user2, foundUser2);

        User foundUser3 = userRepository.find(3L);
        assertNotNull(foundUser3);
        assertEquals(user3, foundUser3);
        assertNull(userRepository.find(4L));

        User user4 = new User("username4", "firstName4", "lastName4", "email4@email.com", LocalDate.now());
        user4.setId(1L);

        assertThrows(RepositoryException.class, () -> userRepository.save(user4));
        assertThrows(IllegalArgumentException.class, () -> userRepository.save(null));
        assertThrows(IllegalArgumentException.class, () -> userRepository.find(null));
    }

    @Test
    void testDeleteMemory() {
        User user1 = new User("username1", "firstName1", "lastName1", "email1@email.com", LocalDate.now());
        user1.setId(1L);
        User user2 = new User("username2", "firstName2", "lastName2", "email2@email.com", LocalDate.now());
        user2.setId(2L);
        User user3 = new User("username3", "firstName3", "lastName3", "email3@email.com", LocalDate.now());
        user3.setId(3L);
        assertNull(userRepository.save(user1));
        assertNull(userRepository.save(user2));
        assertNull(userRepository.save(user3));

        assertEquals(3, StreamSupport.stream(userRepository.findAll().spliterator(), false).count());
        assertEquals(user1, userRepository.delete(user1.getId()));
        assertEquals(2, StreamSupport.stream(userRepository.findAll().spliterator(), false).count());
        assertEquals(user2, userRepository.delete(user2.getId()));
        assertEquals(1, StreamSupport.stream(userRepository.findAll().spliterator(), false).count());
        assertEquals(user3, userRepository.delete(user3.getId()));

        assertThrows(RepositoryException.class, ()-> userRepository.delete(user1.getId()));
        assertThrows(IllegalArgumentException.class, ()-> userRepository.delete(null));
    }

    @Test
    void testUpdateMemory() {
        User user1 = new User("username1", "firstName1", "lastName1", "email1@email.com", LocalDate.now().minusYears(18));
        user1.setId(1L);
        User user2 = new User("username2", "firstName2", "lastName2", "email2@email.com", LocalDate.now().minusYears(18));
        user2.setId(2L);
        User user3 = new User("username3", "firstName3", "lastName3", "email3@email.com", LocalDate.now().minusYears(18));
        user3.setId(3L);
        assertNull(userRepository.save(user1));
        assertNull(userRepository.save(user2));
        assertNull(userRepository.save(user3));
        assertEquals(3, StreamSupport.stream(userRepository.findAll().spliterator(), false).count());

        assertEquals("username1", userRepository.find(1L).getUsername());
        assertEquals("firstName1", userRepository.find(1L).getFirstName());
        assertEquals("lastName1", userRepository.find(1L).getLastName());
        assertEquals("email1@email.com", userRepository.find(1L).getEmail());
        assertEquals(LocalDate.now().minusYears(18), userRepository.find(1L).getBirthDate());

        User user4 = new User("username4", "firstName4", "lastName4", "email4@email.com", LocalDate.now().minusYears(18));
        user4.setId(1L);
        assertEquals(user1, user4);
        assertNull(userRepository.update(user4));

        assertEquals("username4", userRepository.find(1L).getUsername());
        assertEquals("firstName4", userRepository.find(1L).getFirstName());
        assertEquals("lastName4", userRepository.find(1L).getLastName());
        assertEquals("email4@email.com", userRepository.find(1L).getEmail());
        assertEquals(LocalDate.now().minusYears(18), userRepository.find(1L).getBirthDate());
        assertEquals(3, StreamSupport.stream(userRepository.findAll().spliterator(), false).count());

        User user5 = new User("username5", "firstName5", "lastName5", "email5@email.com", LocalDate.now());
        user5.setId(1L);
        assertThrows(RepositoryException.class, ()-> userRepository.save(user5));
        assertThrows(IllegalArgumentException.class, ()-> userRepository.update(null));
    }

    @Test
    void testSaveAndFindFile() {
        assertEquals(10, StreamSupport.stream(userRepositoryFile.findAll().spliterator(), false).count());
        User user11 = new User("username11", "firstName11", "lastName11", "email11@email.com", LocalDate.now());
        user11.setId(11L);
        assertNull(userRepositoryFile.save(user11));
        assertEquals(11, StreamSupport.stream(userRepositoryFile.findAll().spliterator(), false).count());
        assertThrows(IllegalArgumentException.class, ()-> userRepositoryFile.save(null));
        assertThrows(RepositoryException.class, ()-> userRepositoryFile.save(user11));

        User user1 = userRepositoryFile.find(1L);
        assertEquals(1L, user1.getId());
        assertEquals("alex_m", user1.getUsername());
        assertEquals("Alexandra", user1.getFirstName());
        assertEquals("Pop", user1.getLastName());
        assertEquals("alex.pop@email.com", user1.getEmail());
        assertEquals("2000-05-15", user1.getBirthDate().toString());
        assertThrows(IllegalArgumentException.class, ()-> userRepositoryFile.find(null));
        assertNull(userRepositoryFile.find(12L));
    }

    @Test
    void testDeleteFile() {
        assertEquals(10, StreamSupport.stream(userRepositoryFile.findAll().spliterator(), false).count());
        User user1 = userRepositoryFile.find(1L);
        assertEquals(user1, userRepositoryFile.delete(1L));
        assertEquals(9, StreamSupport.stream(userRepositoryFile.findAll().spliterator(), false).count());
        assertThrows(IllegalArgumentException.class, ()-> userRepositoryFile.delete(null));
        assertThrows(RepositoryException.class, ()-> userRepositoryFile.delete(12L));
    }

    @Test
    void testUpdateFile() {
        User user11 = new User("username11", "firstName11", "lastName11", "email11@email.com", LocalDate.now().minusYears(18));
        user11.setId(1L);
        assertEquals(user11, userRepositoryFile.find(1L));
        assertNull(userRepositoryFile.update(user11));
        assertEquals(10, StreamSupport.stream(userRepositoryFile.findAll().spliterator(), false).count());

        assertEquals("username11", userRepositoryFile.find(1L).getUsername());
        assertEquals("firstName11", userRepositoryFile.find(1L).getFirstName());
        assertEquals("lastName11", userRepositoryFile.find(1L).getLastName());
        assertEquals("email11@email.com", userRepositoryFile.find(1L).getEmail());
        assertEquals(LocalDate.now().minusYears(18), userRepositoryFile.find(1L).getBirthDate());
    }

    @Test
    void testAddFindDeleteUpdateFriendshipInMemory() {
        assertNull(friendshipRepository.save(new Friendship(1L, 2L, LocalDateTime.now())));
        assertNull(friendshipRepository.save(new Friendship(1L, 3L, LocalDateTime.of(2026, 10, 12, 10, 30))));
        assertNull(friendshipRepository.save(new Friendship(2L, 5L, LocalDateTime.now().minusDays(5))));
        assertNull(friendshipRepository.save(new Friendship(3L, 6L, LocalDateTime.now().minusDays(6))));
        assertNull(friendshipRepository.save(new Friendship(4L, 7L, LocalDateTime.now().minusDays(7))));
        assertNull(friendshipRepository.save(new Friendship(5L, 8L, LocalDateTime.now().minusDays(8))));
        assertNull(friendshipRepository.save(new Friendship(6L, 9L, LocalDateTime.now().minusDays(9))));
        assertNull(friendshipRepository.save(new Friendship(7L, 10L, LocalDateTime.now().minusDays(10))));
        assertEquals(8, StreamSupport.stream(friendshipRepository.findAll().spliterator(), false).count());
        assertThrows(IllegalArgumentException.class, ()-> friendshipRepository.save(null));

        assertNotNull(friendshipRepository.find(new Tuple<>(1L, 2L)));
        assertNull(friendshipRepository.find(new Tuple<>(1L, 10L)));

        assertEquals(new Friendship(1L, 2L), friendshipRepository.delete(new Tuple<>(1L, 2L)));
        assertNull(friendshipRepository.save(new Friendship(1L, 2L, LocalDateTime.now().minusDays(5))));
        assertEquals(new Friendship(1L, 2L), friendshipRepository.delete(new Tuple<>(2L, 1L)));

        assertEquals(7, StreamSupport.stream(friendshipRepository.findAll().spliterator(), false).count());
        assertThrows(IllegalArgumentException.class, ()-> friendshipRepository.delete(null));
        assertThrows(RepositoryException.class, ()-> friendshipRepository.delete(new Tuple<>(1L, 10L)));

        assertEquals(LocalDateTime.of(2026, 10, 12, 10, 30), friendshipRepository.find(new Tuple<>(1L, 3L)).getFriendsFrom());
        friendshipRepository.update(new Friendship(1L, 3L, LocalDateTime.of(2026, 10, 12, 11, 0)));
        assertEquals(LocalDateTime.of(2026, 10, 12, 11, 0), friendshipRepository.find(new Tuple<>(1L, 3L)).getFriendsFrom());
        assertThrows(IllegalArgumentException.class, ()-> friendshipRepository.update(null));
        assertThrows(RepositoryException.class, ()-> friendshipRepository.update(new Friendship(1L, 2L, LocalDateTime.of(2026, 10, 12, 11, 0))));
    }

    @Test
    void testAddFindDeleteUpdateFriendshipInFile() {
        assertEquals(8, StreamSupport.stream(friendshipRepositoryFile.findAll().spliterator(), false).count());
        assertNull(friendshipRepositoryFile.save(new Friendship(7L, 10L, LocalDateTime.now().minusDays(10))));
        assertEquals(9, StreamSupport.stream(friendshipRepositoryFile.findAll().spliterator(), false).count());
        assertThrows(IllegalArgumentException.class, ()-> friendshipRepositoryFile.save(null));

        assertNotNull(friendshipRepositoryFile.find(new Tuple<>(1L, 2L)));
        assertNotNull(friendshipRepositoryFile.find(new Tuple<>(2L, 1L)));

        assertNull(friendshipRepositoryFile.find(new Tuple<>(1L, 10L)));

        assertEquals(new Friendship(1L, 2L), friendshipRepositoryFile.delete(new Tuple<>(1L, 2L)));
        assertNull(friendshipRepositoryFile.save(new Friendship(1L, 2L, LocalDateTime.now().minusDays(5))));
        assertEquals(new Friendship(1L, 2L), friendshipRepositoryFile.delete(new Tuple<>(2L, 1L)));

        assertEquals(8, StreamSupport.stream(friendshipRepositoryFile.findAll().spliterator(), false).count());
        assertThrows(IllegalArgumentException.class, ()-> friendshipRepositoryFile.delete(null));
        assertThrows(RepositoryException.class, ()-> friendshipRepositoryFile.delete(new Tuple<>(1L, 10L)));

        assertEquals(LocalDateTime.of(2024, 2, 1, 14, 20), friendshipRepositoryFile.find(new Tuple<>(2L, 3L)).getFriendsFrom());
        friendshipRepositoryFile.update(new Friendship(2L, 3L, LocalDateTime.of(2026, 10, 12, 11, 0)));
        assertEquals(LocalDateTime.of(2026, 10, 12, 11, 0), friendshipRepositoryFile.find(new Tuple<>(2L, 3L)).getFriendsFrom());
        assertThrows(IllegalArgumentException.class, ()-> friendshipRepositoryFile.update(null));
        assertThrows(RepositoryException.class, ()-> friendshipRepositoryFile.update(new Friendship(1L, 2L, LocalDateTime.of(2026, 10, 12, 11, 0))));
    }
}