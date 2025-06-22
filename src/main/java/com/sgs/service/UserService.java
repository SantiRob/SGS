package com.sgs.service;

import com.sgs.model.User;
import com.sgs.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public User login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.emailExists(email);
    }

    public boolean registerUser(String name, String email, String role, String password) {
        if (userRepository.emailExists(email)) {
            return false;
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        user.setPassword(password);
        userRepository.save(user);
        return true;
    }
}
