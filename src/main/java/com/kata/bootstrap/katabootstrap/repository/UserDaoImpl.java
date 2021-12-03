package com.kata.bootstrap.katabootstrap.repository;

import com.kata.bootstrap.katabootstrap.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUserByName(String name) {
        return getAllUsers().stream().filter(u -> u.getName().equals(name)).findAny().orElse(null);
    }

    //Получение всех пользователей из БД
    @Override
    public List<User> getAllUsers() {
        Query query = entityManager.createQuery("Select distinct e from User e join fetch e.roles", User.class);
        List<User> userList = query.getResultList();

        return userList;
    }

    //получение юзера по ID
    @Override
    public User getUser(Long id) {
        return entityManager.find(User.class, id);
    }

    //Добавление нового пользователя
    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    //Обновление пользователя
    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    //Удаление пользователя по ID
    @Override
    public void removeUserById(Long id) {
        System.out.println(getUser(id));
        try {
            entityManager.remove(getUser(id));
        } catch (Exception e) {
            System.out.println("Пользователь не существует!!!");
        }
    }
}

