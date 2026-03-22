package org.martini.backend.event;

import lombok.RequiredArgsConstructor;
import org.martini.backend.model.EmailDto;
import org.martini.backend.service.EmailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserRegisteredEventListener {

    private final EmailSender emailSender;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {

        String subject = "Registration Confirmation";
        String content = "Thank you for registering. Please use the following token to verify your account: "
                + event.token();

        emailSender.sendMail(
                EmailDto.builder()
                        .from("noreply@martini.com")
                        .to(event.email())
                        .subject(subject)
                        .content(content)
                        .build()
        );
    }
}
