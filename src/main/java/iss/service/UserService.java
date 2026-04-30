package iss.service;

import iss.exceptions.AuthenticationException;
import iss.model.Club;
import iss.model.User;
import iss.model.UserType;
import iss.repository.UserRepository;
import iss.validation.UserValidator;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class UserService {
    private UserRepository userRepository;
    private UserValidator userValidator;

    public UserService(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public User login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                return user.get();
            }
        }

        if (user.isEmpty())
            throw new AuthenticationException("Username or password incorrect");

        throw new AuthenticationException("Username or password incorrect");
    }

    public User addUser(String username, String password, UserType userType, Club club) {
        User user = new User(null, username, password, userType, club);
        userValidator.validate(user);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        }

        throw new EntityNotFoundException("User not found");
    }

    public List<User> getUsersByClub(Club club) {
        return userRepository.findByClub(club);
    }

    public boolean deleteUser(Long id) {
        return userRepository.delete(userRepository.findById(id).get());
    }

    public User updateUser(User user) {
        return userRepository.update(user);
    }
}
