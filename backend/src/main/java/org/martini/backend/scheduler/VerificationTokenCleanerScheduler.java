package org.martini.backend.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.martini.backend.service.VerificationTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log
@Component
@RequiredArgsConstructor
public class VerificationTokenCleanerScheduler {

    private final VerificationTokenService verificationTokenService;

    @Scheduled(fixedDelayString = "PT5M")
    @Transactional
    public void processEmailQueue() {
        long countExpiredTokens = verificationTokenService.countExpiredTokens();
        if (countExpiredTokens > 0) {
            log.info("Deleting expired tokens. In total: " + countExpiredTokens + " will be deleted");
            verificationTokenService.deleteExpiredTokens();
        }
    }
}
