package services;

import models.User;
import repository.impl.UserRepositoryImpl;

import java.util.List;

public class UserService {

    private final UserRepositoryImpl userRepository;


    public UserService(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public void createUser(User newUser) {
        if (userRepository.findAll().stream()
                .anyMatch(user -> user.getUsername().equals(newUser.getUsername()))) {
            throw new IllegalArgumentException(String.format("A User with the Username %s already exists.", newUser.getUsername()));
        }
        this.userRepository.save(newUser);
    }

}
