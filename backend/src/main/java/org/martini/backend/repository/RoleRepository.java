package org.martini.backend.repository;

import org.martini.backend.model.dao.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<UserRole, Long> {
  Optional<UserRole> findByName(String name);
}
