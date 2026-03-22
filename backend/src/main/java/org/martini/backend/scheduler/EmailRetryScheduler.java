package org.martini.backend.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.martini.backend.model.EmailDto;
import org.martini.backend.model.dao.EmailQueue;
import org.martini.backend.repository.EmailQueueRepository;
import org.martini.backend.service.EmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailRetryScheduler {

    private final EmailQueueRepository emailQueueRepository;
    private final EmailSender emailSender;

    @Value("${email.retry.max-attempts:5}")
    private int maxAttempts;

    @Value("${email.retry.interval-minutes:5}")
    private int retryIntervalMinutes;

    @Scheduled(fixedDelay = 30000) // Run every minute
    public void processEmailQueue() {
        List<EmailQueue> pendingEmails = emailQueueRepository
                .findAllBySentFalseAndFailedFalseAndNextAttemptAtBefore(LocalDateTime.now());

        for (EmailQueue email : pendingEmails) {
            try {
                emailSender.sendMail(EmailDto.builder()
                        .from(email.getFrom())
                        .to(email.getTo())
                        .subject(email.getSubject())
                        .content(email.getContent())
                        .build());
                email.setSent(true);
                log.info("Email sent successfully to {}", email.getTo());
            } catch (Exception e) {
                log.error("Failed to send email to {}: {}", email.getTo(), e.getMessage());
                email.setRetryCount(email.getRetryCount() + 1);
                if (email.getRetryCount() >= maxAttempts) {
                    email.setFailed(true);
                    log.error("Email to {} failed after {} attempts", email.getTo(), maxAttempts);
                } else {
                    email.setNextAttemptAt(LocalDateTime.now().plusMinutes(retryIntervalMinutes));
                }
            }
            emailQueueRepository.save(email);
        }
    }
}
