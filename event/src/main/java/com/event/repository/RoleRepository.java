package com.event.repository;

import com.event.entity.Role;
import com.event.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @NativeQuery("SELECT * FROM roles " +
                    "WHERE (name = ?1)")
    Optional<Role> getByName(String roleName);
}
