package com.kata.bootstrap.katabootstrap.repository;

import com.kata.bootstrap.katabootstrap.entity.Role;

import java.util.List;

public interface RoleDao {
    Role getRoleByName(String name);

    Role getRoleById(Long id);

    List<Role> getAllRoles();
}
