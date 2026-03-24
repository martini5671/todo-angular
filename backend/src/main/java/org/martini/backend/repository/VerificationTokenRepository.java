package org.martini.backend.repository;

import org.martini.backend.model.dao.User;
import org.martini.backend.model.dao.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUser(User user);

    long countByExpiryDateBefore(LocalDateTime now);
    void deleteAllByExpiryDateBefore(LocalDateTime now);
}
