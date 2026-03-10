package org.martini.backend.util;

import lombok.RequiredArgsConstructor;
import org.martini.backend.model.dao.User;
import org.martini.backend.model.dao.UserRole;
import org.martini.backend.repository.RoleRepository;
import org.martini.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  @Override
  @Transactional
  public void run(String... args) {
    if (roleRepository.count() == 0) {
      roleRepository.saveAndFlush(
        UserRole.builder()
          .name("ROLE_ADMIN")
          .build()
      );
    }

    if (userRepository.count() == 0) {
      userRepository.save(
        User.builder()
          .username("admin")
          .userRoles(
            List.of(
              roleRepository.findByName("ROLE_ADMIN").orElseThrow()
            )
          )
          .password(passwordEncoder.encode("admin"))
          .build()
      );
    }
  }
}
