package com.example.social_network.service;

import com.example.social_network.domain.Friendship;
import com.example.social_network.domain.Tuple;
import com.example.social_network.domain.User;
import com.example.social_network.repository.Repository;
import com.example.social_network.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class NetworkService {
    private final Repository<Long, User> userRepository;
    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;
    private final Validator<User> userValidator;
    private final Validator<Friendship> friendshipValidator;

    public void addUser(Long id, String username, String firstName, String lastName, String email, LocalDate birthDate) {
        try {
            User user = new User(username, firstName, lastName, email, birthDate);
            user.setId(id);

            userValidator.validate(user);
            userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void removeUser(Long id) {
        try {
            User user = userRepository.find(id);
            if (user == null) {
                throw new ServiceException(id + " was not deleted because it doesn't exist");
            }

            List<Tuple<Long, Long>> friendshipsToRemove = new ArrayList<>();
            for (Friendship f: friendshipRepository.findAll()) {
                if (f.getIdUser1().equals(id) || f.getIdUser2().equals(id)) {
                    friendshipsToRemove.add(f.getId());
                }
            }

            for (Tuple<Long, Long> friendshipToRemove : friendshipsToRemove) {
                friendshipRepository.delete(friendshipToRemove);
            }

            userRepository.delete(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void updateUser(Long id, String username, String firstName, String lastName, String email, LocalDate birthDate) {
        try {
            User user = new User(username, firstName, lastName, email, birthDate);
            user.setId(id);
            userValidator.validate(user);
            userRepository.update(user);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public List<Friendship> getAllFriendships() {
        return StreamSupport.stream(friendshipRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public void addFriendship(Long user1Id, Long user2Id, LocalDateTime friendsFrom) {
        try {
            Friendship friendship = new Friendship(user1Id, user2Id, friendsFrom);
            friendshipValidator.validate(friendship);

            if (userRepository.find(user1Id) == null ) {
                throw new ServiceException(user1Id + " doesn't exist in the system");
            }

            if (userRepository.find(user2Id) == null ) {
                throw new ServiceException(user2Id + " doesn't exist in the system");
            }

            friendshipRepository.save(friendship);
        }

        catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void addFriendship(Long user1Id, Long user2Id) {
        try {
            Friendship friendship = new Friendship(user1Id, user2Id);
            friendshipValidator.validate(friendship);

            if (userRepository.find(user1Id) == null ) {
                throw new ServiceException(user1Id + " doesn't exist in the system");
            }

            if (userRepository.find(user2Id) == null ) {
                throw new ServiceException(user2Id + " doesn't exist in the system");
            }

            friendshipRepository.save(friendship);
        }

        catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public User findUser(Long id) {
        try {
            User user = userRepository.find(id);
            if (user == null) {
                throw new ServiceException("A user with the id " + id + " doesn't exist in the system");
            }
            return user;
        }

        catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Friendship findFriendship(Long user1Id, Long user2Id) {
        try {
            Friendship friendship = friendshipRepository.find(new Tuple<>(user1Id, user2Id));
            if (friendship == null) {
                throw new ServiceException("A friendship between the users with the id's " + user1Id + " and " + user2Id + " doesn't exist in the system");
            }
            return friendship;
        }

        catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void removeFriendship(Long user1Id, Long user2Id) {
        try {
            if (user1Id == null || user2Id == null) {
                throw new ServiceException("One or both user id's are null");
            }

            friendshipRepository.delete(new Tuple<>(user1Id, user2Id));
        }

        catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Map<Long, List<Long>> buildAdjacencyList() {
        Map<Long, List<Long>> adjacencyList = new HashMap<>();

        for (User user: userRepository.findAll()) {
            adjacencyList.put(user.getId(), new ArrayList<>());
        }

        for (Friendship friendship: friendshipRepository.findAll()) {
            Long user1Id = friendship.getIdUser1();
            Long user2Id = friendship.getIdUser2();

            if (adjacencyList.containsKey(user1Id) && adjacencyList.containsKey(user2Id)) {
                adjacencyList.get(user1Id).add(user2Id);
                adjacencyList.get(user2Id).add(user1Id);
            }
        }

        return adjacencyList;
    }

    public int getNumberOfComunities() {
        Map<Long, List<Long>> adjacencyList = buildAdjacencyList();
        Set<Long> visited = new HashSet<>();
        int count = 0;

        for (Long userId: adjacencyList.keySet()) {
            if (!visited.contains(userId)) {
                count++;
                dfs(userId, adjacencyList, visited, null);
            }
        }

        return count;
    }

    public void dfs(Long startNode, Map<Long, List<Long>> adjacencyList, Set<Long> visited, List<Long> component) {
        visited.add(startNode);
        if (component != null) {
            component.add(startNode);
        }
        for (Long nodeId: adjacencyList.get(startNode)) {
            if (!visited.contains(nodeId)) {
                dfs(nodeId, adjacencyList, visited, component);
            }
        }
    }

    public int getLongestPathInComponent(List<Long> component, Map<Long, List<Long>> adjacencyList) {
        int maxDist = 0;

        for (Long startNode: component) {
            Map<Long, Integer> distances = new HashMap<>();
            Queue<Long> queue = new LinkedList<>();

            queue.add(startNode);
            distances.put(startNode, 0);

            while (!queue.isEmpty()) {
                Long currentNode = queue.poll();
                int dist = distances.get(currentNode);
                maxDist = Math.max(maxDist, dist);

                for (Long neighbour: adjacencyList.get(currentNode)) {
                    if (!distances.containsKey(neighbour)) {
                        distances.put(neighbour, dist + 1);
                        queue.add(neighbour);
                    }
                }
            }
        }
        return maxDist;
    }

    public List<User> getMostSociableComunity() {
        Map<Long, List<Long>> adjacencyList = buildAdjacencyList();
        Set<Long> visited = new HashSet<>();

        List<Long> mostSociableComunity = new ArrayList<>();
        int maxPathLength = -1;

        for (Long userId: adjacencyList.keySet()) {
            if (!visited.contains(userId)) {
                List<Long> currentComponent = new ArrayList<>();
                dfs(userId, adjacencyList, visited, currentComponent);

                int currentMaxPath = getLongestPathInComponent(currentComponent, adjacencyList);

                if (currentMaxPath > maxPathLength) {
                    maxPathLength = currentMaxPath;
                    mostSociableComunity = currentComponent;
                }
            }
        }

        return mostSociableComunity.stream().map(this::findUser).collect(Collectors.toList());
    }
}