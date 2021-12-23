package com.kata.bootstrap.katabootstrap.service;

import com.kata.bootstrap.katabootstrap.entity.User;

import java.util.List;

public interface UserService {
    User getUser(Long id);

    User getUserByName(String name);

    public User getUserByEmail(String email);

    List<User> getAllUsers();

    void addUser(User user);

    void removeUserById(Long id);

    void updateUser(Long id, User user);
}

