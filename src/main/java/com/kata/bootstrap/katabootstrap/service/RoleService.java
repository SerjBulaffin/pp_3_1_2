package com.kata.bootstrap.katabootstrap.service;

import com.kata.bootstrap.katabootstrap.entity.Role;
import com.kata.bootstrap.katabootstrap.repository.RoleDao;

import java.util.List;

public interface RoleService {

    Role getRoleByName(String name);

    Role getRoleById(Long id);

    List<Role> allRoles();
}
