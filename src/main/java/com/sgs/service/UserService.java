package com.sgs.service;

import com.sgs.model.User;
import com.sgs.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public User login(String sapUser, String password) {
        return userRepository.findBySapUserAndPassword(sapUser, password);
    }

    public boolean isSapUserRegistered(String sapUser) {
        return userRepository.sapUserExists(sapUser);
    }

    public boolean registerUser(String name, String sapUser, String email, String role, String password) {
        if (userRepository.sapUserExists(sapUser)) {
            return false;
        }

        User user = new User();
        user.setName(name);
        user.setSapUser(sapUser);
        user.setEmail(email);
        user.setRole(role);
        user.setPassword(password);

        userRepository.save(user);
        return true;
    }
}
