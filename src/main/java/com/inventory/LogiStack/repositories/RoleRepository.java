package com.inventory.LogiStack.repositories;

import com.inventory.LogiStack.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(String role);

    @Query("select r from Role r where r.name = ?1")
    Role findRole(String role);

    @Query("select r from Role r where r.id = ?1")
    Optional<Role> findRoleById(long id);
}
