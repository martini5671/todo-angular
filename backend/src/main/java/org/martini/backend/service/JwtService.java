package org.martini.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class JwtService {

  private static final String SUBJECT = "User Details";
  private static final String ISSUER = "space_management";
  private static final String USERNAME_CLAIM_NAME = "username";
  private static final String ROLES_CLAIM_NAME = "roles";
  private JWTVerifier verifier;

  @Value("${auth.token-expiration-hours}")
  private int expirationHours;

  @Value("${auth.jwt-secret-key}")
  private String secret;

  @PostConstruct
  void init() {
    verifier = JWT.require(Algorithm.HMAC256(secret))
      .withSubject(SUBJECT)
      .withIssuer(ISSUER)
      .build();
  }

  public String generateToken(String username, List<String> roles) {
    Objects.requireNonNull(username, "Username cannot be null");
    Objects.requireNonNull(roles, "Roles cannot be null");
    Assert.isTrue(!roles.isEmpty(), "Roles cannot be empty");
    return JWT.create()
      .withSubject(SUBJECT)
      .withClaim(USERNAME_CLAIM_NAME, username)
      .withIssuedAt(new Date())
      .withExpiresAt(Instant.now().plus(Duration.ofHours(expirationHours)))
      .withIssuer(ISSUER)
      .withClaim(ROLES_CLAIM_NAME, roles)
      .sign(Algorithm.HMAC256(secret));
  }

  public String getUsername(String token) {
    return verifier.verify(token)
      .getClaim(USERNAME_CLAIM_NAME).asString();
  }

  public List<String> getRoles(String token) {
    return verifier.verify(token).getClaim(ROLES_CLAIM_NAME)
      .asList(String.class);
  }

}
