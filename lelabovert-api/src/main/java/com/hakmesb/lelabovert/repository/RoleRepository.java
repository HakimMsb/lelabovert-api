package com.hakmesb.lelabovert.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hakmesb.lelabovert.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	Optional<Role> findOneByName(String name);
	
}
