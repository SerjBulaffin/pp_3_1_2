package com.kata.bootstrap.katabootstrap.repository;

import com.kata.bootstrap.katabootstrap.entity.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Role getRoleByName(String name) {
        return getAllRoles().stream().filter(u -> u.getRole().equals(name)).findAny().orElse(null);
    }

    @Override
    public Role getRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public List<Role> getAllRoles() {
        Query query = entityManager.createQuery("Select r from Role r", Role.class);
        List<Role> roleList = query.getResultList();
        //System.out.println("public List<Role> getAllRoles() - " + roleList);
        return roleList;
    }
}
