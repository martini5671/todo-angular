package org.martini.backend.service;

import lombok.RequiredArgsConstructor;
import org.martini.backend.model.dao.User;
import org.martini.backend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("No user with username " + username +
        " was found"));
    return new org.springframework.security.core.userdetails.User(
      username,
      user.getPassword(),
      user.getUserRoles().stream()
        .map(userRole -> new SimpleGrantedAuthority(userRole.getName()))
        .toList()
    );

  }
}
