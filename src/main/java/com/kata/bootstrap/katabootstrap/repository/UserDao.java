package com.kata.bootstrap.katabootstrap.repository;

import com.kata.bootstrap.katabootstrap.entity.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    User getUserByName(String name);
    User getUserByEmail(String email);

    void addUser(User user);
    void updateUser(User user);
    void removeUserById(Long id);
    User getUser(Long id);

    //void addUserDB();
}
