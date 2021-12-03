package com.kata.bootstrap.katabootstrap.service;

import com.kata.bootstrap.katabootstrap.entity.Role;
import com.kata.bootstrap.katabootstrap.repository.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl {

    private RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Role getRoleByName(String name) {
        return roleDao.getRoleByName(name);
    }

    public Role getRoleById(Long id) {
        return roleDao.getRoleById(id);
    }

    public List<Role> allRoles() {
        return roleDao.getAllRoles();
    }
}
