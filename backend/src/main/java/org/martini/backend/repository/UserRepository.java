package org.martini.backend.repository;

import org.martini.backend.model.dao.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(attributePaths = "userRoles")
  Optional<User> findByUsername(String username);
  boolean existsByUsername(String username);
}
