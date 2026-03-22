package org.martini.backend.service;

import lombok.RequiredArgsConstructor;
import org.martini.backend.model.dao.User;
import org.martini.backend.model.dao.VerificationToken;
import org.martini.backend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Value("${auth.verification-token-expiration-minutes}")
    private int expirationMinutes;

    @Transactional
    public void createToken(User user) {
        String token = UUID.randomUUID().toString();

        verificationTokenRepository.findByUser(user)
                .ifPresent(verificationTokenRepository::delete);

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(expirationMinutes))
                .build();

        verificationTokenRepository.save(verificationToken);
    }

    public boolean isTokenValid(String token) {
        return verificationTokenRepository.findByToken(token)
                .map(verificationToken -> !verificationToken.isExpired())
                .orElse(false);
    }

    @Transactional
    public void deleteExpiredTokens() {
        verificationTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
    }
}
