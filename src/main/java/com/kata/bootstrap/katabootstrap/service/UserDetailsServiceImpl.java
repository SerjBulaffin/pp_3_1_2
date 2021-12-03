package com.kata.bootstrap.katabootstrap.service;

import com.kata.bootstrap.katabootstrap.entity.User;
import com.kata.bootstrap.katabootstrap.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDao userDao;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    // «Пользователь» – это просто Object. В большинстве случаев он может быть
    //  приведен к классу UserDetails.
    // Для создания UserDetails используется интерфейс UserDetailsService, с единственным методом:
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.getUserByName(s);
    }

    //получение юзера по ID
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }


    public void addUser(User user) {
        userDao.addUser(user);
    }


    public void removeUserById(Long id) {
        userDao.removeUserById(id);
    }


    public void updateUser(Long id, User user) {
        userDao.updateUser(user);
    }
}
