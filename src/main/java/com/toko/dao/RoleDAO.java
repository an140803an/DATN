package com.toko.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toko.entity.Role;



@Repository
public interface RoleDAO extends JpaRepository<Role, String>{

}
