package org.martini.backend.service;

import lombok.RequiredArgsConstructor;
import org.martini.backend.exception.ResourceNotFoundException;
import org.martini.backend.model.dao.User;
import org.martini.backend.model.dao.VerificationToken;
import org.martini.backend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Value("${auth.verification-token-expiration-minutes}")
    private int expirationMinutes;

    @Transactional
    public VerificationToken createVerificationTokenForUser(User user) {
        String token = UUID.randomUUID().toString();

        verificationTokenRepository.findByUser(user)
                .ifPresent(verificationTokenRepository::delete);

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(expirationMinutes))
                .build();

        return verificationTokenRepository.save(verificationToken);
    }

    public boolean isTokenValid(String token) {
        return verificationTokenRepository.findByToken(token)
                .map(verificationToken -> !verificationToken.isExpired())
                .orElse(false);
    }

    public VerificationToken findVerificationTokenByTokenValue(String tokenValue) {
        return verificationTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new ResourceNotFoundException("no such token exists"));
    }

    public void saveToken(VerificationToken token) {
        verificationTokenRepository.save(token);
    }

    public void deleteExpiredTokens() {
        verificationTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
    }

    public long countExpiredTokens() {
       return verificationTokenRepository.countByExpiryDateBefore(LocalDateTime.now());
    }
}
