package org.martini.backend.event;

import lombok.RequiredArgsConstructor;
import org.martini.backend.model.dao.EmailQueue;
import org.martini.backend.repository.EmailQueueRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserRegisteredEventListener {

    private final EmailQueueRepository emailQueueRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {

        String subject = "Registration Confirmation";
        String content = "Thank you for registering. Please click following link to verify your account: "
                + event.link();

        emailQueueRepository.save(
                EmailQueue.builder()
                        .from("noreply@martini.com")
                        .to(event.email())
                        .subject(subject)
                        .content(content)
                        .retryCount(0)
                        .nextAttemptAt(LocalDateTime.now())
                        .sent(false)
                        .failed(false)
                        .build()
        );
    }
}
